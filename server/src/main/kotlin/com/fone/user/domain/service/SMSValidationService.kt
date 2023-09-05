package com.fone.user.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.common.redis.RedisRepository
import com.fone.user.domain.entity.User
import com.fone.user.domain.repository.UserRepository
import com.fone.user.domain.repository.generateRandomCode
import com.fone.user.presentation.dto.SMSUserDto.PasswordSMSValidationResponse
import com.fone.user.presentation.dto.SMSUserDto.ResponseType
import com.fone.user.presentation.dto.SMSUserDto.SMSRequest
import com.fone.user.presentation.dto.SMSUserDto.SMSResponse
import com.fone.user.presentation.dto.SMSUserDto.SMSValidationRequest
import com.fone.user.presentation.dto.SMSUserDto.UserInfoSMSValidationResponse
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.TimeUnit

@Service
class SMSValidationService(
    private val redisRepository: RedisRepository,
    private val userRepository: UserRepository,
) {
    suspend fun sendSMS(request: SMSRequest, smsSender: suspend (phone: String, code: String) -> Unit): SMSResponse {
        val user = userRepository.findByPhone(request.phoneNumber)
            ?: throw NotFoundUserException()
        val code = createUpdateVerificationCode(user)
        smsSender(user.phoneNumber!!, code)
        return SMSResponse(ResponseType.SUCCESS)
    }

    suspend fun validateSMSPassword(request: SMSValidationRequest): PasswordSMSValidationResponse {
        val user = userRepository.findByPhone(request.phoneNumber)
            ?: throw NotFoundUserException()
        if (!validateVerificationMessage(user, request.code)) {
            return PasswordSMSValidationResponse(ResponseType.FAILURE)
        }
        val token = (UUID.randomUUID().toString() + UUID.randomUUID().toString()).replace("-", "")
        redisRepository.setValue("user:${user.phoneNumber}:passwordUpdate", token, 30, TimeUnit.MINUTES)
        return PasswordSMSValidationResponse(ResponseType.SUCCESS, token)
    }

    suspend fun validateSMSUserInfo(request: SMSValidationRequest): UserInfoSMSValidationResponse {
        val user = userRepository.findByPhone(request.phoneNumber)
            ?: throw NotFoundUserException()
        return UserInfoSMSValidationResponse(ResponseType.SUCCESS, user.loginType, user.email)
    }

    private suspend fun createUpdateVerificationCode(user: User): String {
        val code = generateRandomCode()
        redisRepository.setValue("user:${user.phoneNumber}:passwordPhoneValidation", code, 185, TimeUnit.SECONDS)
        return code
    }

    private suspend fun validateVerificationMessage(user: User, code: String): Boolean {
        return redisRepository.getValue("user:${user.phoneNumber}:passwordPhoneValidation") == code
    }
}
