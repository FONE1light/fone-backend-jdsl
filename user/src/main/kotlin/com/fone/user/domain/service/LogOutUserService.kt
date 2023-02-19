package com.fone.user.domain.service

import com.fone.common.redis.RedisRepository
import org.springframework.stereotype.Service

@Service
class LogOutUserService(
    private val redisRepository: RedisRepository,
) {

    suspend fun logOutUser(email: String) {
        redisRepository.delValue(redisRepository.REFRESH_PREFIX + "$email")
    }
}