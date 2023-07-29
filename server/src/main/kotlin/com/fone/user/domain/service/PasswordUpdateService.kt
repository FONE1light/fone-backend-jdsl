package com.fone.user.domain.service

import com.fone.common.exception.GlobalException
import com.fone.common.password.PasswordService
import com.fone.common.redis.RedisRepository
import com.fone.user.domain.entity.User
import com.fone.user.domain.repository.UserRepository
import com.fone.user.presentation.dto.PasswordUpdateDto
import com.fone.user.presentation.dto.PasswordUpdateDto.PasswordUpdateRequest
import com.fone.user.presentation.dto.PasswordUpdateDto.PasswordUpdateResponse
import com.fone.user.presentation.dto.SMSUserDto.ResponseType
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class PasswordUpdateService(
    private val redisRepository: RedisRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    suspend fun updateRequest(request: PasswordUpdateRequest): PasswordUpdateResponse {
        val user = userRepository.findByPhone(request.phoneNumber)
            ?: throw GlobalException(HttpStatus.BAD_REQUEST, "User Not Found")
        val response = redisRepository.getValue("user:${user.phoneNumber}:passwordUpdate")
            ?: throw GlobalException(HttpStatus.BAD_REQUEST, "User가 현재 비밀번호 업데이트 상태가 아님")
        if (response != request.token) return PasswordUpdateResponse(PasswordUpdateDto.ResponseType.INVALID_TOKEN)
        runCatching {
            updateUserWithPassword(user, request.password)
            redisRepository.delValue("user:${user.phoneNumber}:passwordUpdate")
        }.onFailure {
            it.printStackTrace()
            throw GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, "User 비밀번호 업데이트 실패")
        }
        return PasswordUpdateResponse(PasswordUpdateDto.ResponseType.SUCCESS)
    }
    private suspend fun updateUserWithPassword(user: User, password: String) {
        user.password = PasswordService.hashPassword(password)
        userRepository.save(user)
    }
}
