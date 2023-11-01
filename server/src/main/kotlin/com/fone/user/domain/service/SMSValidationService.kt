package com.fone.user.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.common.exception.SMSValidationException
import com.fone.common.redis.RedisRepository
import com.fone.user.domain.entity.User
import com.fone.user.domain.enum.LoginType
import com.fone.user.domain.repository.UserRepository
import com.fone.user.domain.repository.generateRandomCode
import com.fone.user.presentation.dto.SMSUserDto.PasswordSMSValidationResponse
import com.fone.user.presentation.dto.SMSUserDto.SMSRequest
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
    suspend fun sendSMS(request: SMSRequest, smsSender: suspend (phone: String, code: String) -> Unit) {
        val user = userRepository.findByPhone(request.phoneNumber)
            ?: throw NotFoundUserException()
        val code = createUpdateVerificationCode(user)
        smsSender(user.phoneNumber!!, code)
    }

    suspend fun validateSMSPassword(request: SMSValidationRequest): PasswordSMSValidationResponse {
        val user = userRepository.findByPhone(request.phoneNumber)
            ?: throw NotFoundUserException()
        return if (user.loginType == LoginType.PASSWORD) {
            if (!isValidCode(user, request.code)) {
                throw SMSValidationException()
            }
            val token = (UUID.randomUUID().toString() + UUID.randomUUID().toString()).replace("-", "")
            redisRepository.setValue("user:passwordUpdate:${user.phoneNumber}", token, 30, TimeUnit.MINUTES)
            PasswordSMSValidationResponse(token, user.loginType)
        } else {
            PasswordSMSValidationResponse(null, user.loginType)
        }
    }

    suspend fun validateSMSUserInfo(request: SMSValidationRequest): UserInfoSMSValidationResponse {
        if (!isValidCode(request.phoneNumber, request.code)) {
            throw SMSValidationException()
        }
        val user = userRepository.findByPhone(request.phoneNumber)
            ?: throw NotFoundUserException()
        return UserInfoSMSValidationResponse(user.loginType, user.email)
    }

    private suspend fun createUpdateVerificationCode(user: User): String {
        return createUpdateVerificationCode(user.phoneNumber!!)
    }

    private suspend fun createUpdateVerificationCode(phoneNumber: String): String {
        val code = generateRandomCode()
        redisRepository.setValue("user:smsValidation:$phoneNumber", code, 185, TimeUnit.SECONDS)
        return code
    }

    private suspend fun isValidCode(user: User, code: String): Boolean {
        return isValidCode(user.phoneNumber!!, code)
    }

    private suspend fun isValidCode(phoneNumber: String, code: String): Boolean {
        return redisRepository.getValue("user:smsValidation:$phoneNumber") == code
    }
}
