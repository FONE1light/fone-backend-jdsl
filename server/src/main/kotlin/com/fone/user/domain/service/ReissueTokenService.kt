package com.fone.user.domain.service

import com.fone.common.exception.InvalidTokenException
import com.fone.common.jwt.JWTUtils
import com.fone.common.jwt.Token
import com.fone.common.redis.RedisRepository
import com.fone.user.presentation.dto.ReissueTokenDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Service
class ReissueTokenService(
    private val jwtUtils: JWTUtils,
    private val redisRepository: RedisRepository,
) {

    @Transactional(readOnly = true)
    suspend fun reissueToken(request: ReissueTokenDto.ReissueTokenRequest, email: String): Token {
        if (!jwtUtils.validateToken(request.refreshToken)) throw InvalidTokenException()

        val refreshToken =
            redisRepository.getValue(redisRepository.REFRESH_PREFIX + email) ?: throw InvalidTokenException()

        val token = jwtUtils.reissue(email, request.accessToken)

        redisRepository.setValue(
            redisRepository.REFRESH_PREFIX + email,
            token.refreshToken,
            jwtUtils.refreshTokenValidityInMilliseconds,
            TimeUnit.MILLISECONDS
        )

        return token
    }
}
