package com.fone.user.domain.service

import com.fone.common.exception.InvalidTokenException
import com.fone.common.jwt.JWTUtils
import com.fone.common.jwt.Token
import com.fone.common.redis.RedisRepository
import com.fone.user.presentation.dto.ReissueTokenRequest
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.stereotype.Service

@Service
class ReissueTokenService(
    private val jwtUtils: JWTUtils,
    private val redisRepository: RedisRepository,
) {

    suspend fun reissueToken(request: ReissueTokenRequest): Token {
        validateRefreshToken(request.refreshToken)
        return jwtUtils.reissueAccessToken(request.refreshToken)
    }

    private fun validateRefreshToken(token: String) {
        try {
            if (!jwtUtils.validateToken(token)) throw InvalidTokenException()
        } catch (e: ExpiredJwtException) {
            throw InvalidTokenException()
        }
        val email = jwtUtils.getEmailFromToken(token)
        redisRepository.getValue(redisRepository.REFRESH_PREFIX + email) ?: throw InvalidTokenException()
    }
}
