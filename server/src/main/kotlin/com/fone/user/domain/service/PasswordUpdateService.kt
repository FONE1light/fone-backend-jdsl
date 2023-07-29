package com.fone.user.domain.service

import com.fone.common.exception.GlobalException
import com.fone.common.password.PasswordService
import com.fone.common.redis.RedisRepository
import com.fone.user.domain.entity.User
import com.fone.user.domain.enum.LoginType
import com.fone.user.domain.repository.SMSRepository
import com.fone.user.domain.repository.UserRepository
import com.fone.user.presentation.dto.PasswordPhoneDto.PasswordPhoneSMSRequest
import com.fone.user.presentation.dto.PasswordPhoneDto.PasswordPhoneSMSResponse
import com.fone.user.presentation.dto.PasswordPhoneDto.PasswordPhoneValidationRequest
import com.fone.user.presentation.dto.PasswordPhoneDto.PasswordPhoneValidationResponse
import com.fone.user.presentation.dto.PasswordUpdateDto
import com.fone.user.presentation.dto.PasswordUpdateDto.PasswordUpdateRequest
import com.fone.user.presentation.dto.PasswordUpdateDto.PasswordUpdateResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.transaction.Transactional

@Service
class PasswordUpdateService(
    private val redisRepository: RedisRepository,
    private val userRepository: UserRepository,
    private val phoneService: AuthenticatePhoneService,
    private val smsRepository: SMSRepository,
) {
    @Transactional
    suspend fun updateRequest(request: PasswordUpdateRequest): PasswordUpdateResponse {
        val user = userRepository.findByEmailAndLoginType(request.email, LoginType.PASSWORD)
            ?: throw GlobalException(HttpStatus.BAD_REQUEST, "User Not Found")
        val response = redisRepository.getValue("user:${user.email}:passwordUpdate")
            ?: throw GlobalException(HttpStatus.BAD_REQUEST, "User가 현재 비밀번호 업데이트 상태가 아님")
        if (response != request.token) return PasswordUpdateResponse(PasswordUpdateDto.ResponseType.INVALID_TOKEN)
        runCatching {
            updateUserWithPassword(user, request.password)
            redisRepository.delValue("user:${user.email}:passwordUpdate")
        }.onFailure {
            it.printStackTrace()
            throw GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, "User 비밀번호 업데이트 실패")
        }
        return PasswordUpdateResponse(PasswordUpdateDto.ResponseType.SUCCESS)
    }

    suspend fun sendSMS(request: PasswordPhoneSMSRequest): PasswordPhoneSMSResponse {
        val user = userRepository.findByEmailAndLoginType(request.email, LoginType.PASSWORD)
            ?: throw GlobalException(HttpStatus.BAD_REQUEST, "User Not Found")
        sendUpdateVerificationMessage(user)
        return PasswordPhoneSMSResponse(ResponseType.SUCCESS)
    }

    suspend fun validateSMS(request: PasswordPhoneValidationRequest): PasswordPhoneValidationResponse {
        val user = userRepository.findByEmailAndLoginType(request.email, LoginType.PASSWORD)
            ?: throw GlobalException(HttpStatus.BAD_REQUEST, "User Not Found")
        if (!validateVerificationMessage(user, request.code)) {
            return PasswordPhoneValidationResponse(ResponseType.FAILURE)
        }
        val token = (UUID.randomUUID().toString() + UUID.randomUUID().toString()).replace("-", "")
        redisRepository.setValue("user:${user.email}:passwordUpdate", token, 30, TimeUnit.MINUTES)
        return PasswordPhoneValidationResponse(ResponseType.SUCCESS, token)
    }

    private suspend fun sendUpdateVerificationMessage(user: User) {
        val code = smsRepository.generateRandomCode()
        redisRepository.setValue("user:${user.email}:passwordPhoneValidation", code, 185, TimeUnit.SECONDS)
        phoneService.sendMessageToPhone(user.phoneNumber!!, code)
    }

    private suspend fun validateVerificationMessage(user: User, code: String): Boolean {
        return redisRepository.getValue("user:${user.email}:passwordPhoneValidation") == code
    }
    private suspend fun updateUserWithPassword(user: User, password: String) {
        user.password = PasswordService.hashPassword(password)
        userRepository.save(user)
    }
}
