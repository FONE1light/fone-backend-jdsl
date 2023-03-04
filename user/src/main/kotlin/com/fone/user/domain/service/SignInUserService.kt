package com.fone.user.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.common.jwt.JWTUtils
import com.fone.common.jwt.Role
import com.fone.common.redis.RedisRepository
import com.fone.user.domain.repository.UserRepository
import com.fone.user.presentation.dto.SignInUserDto.SignInUserRequest
import com.fone.user.presentation.dto.SignInUserDto.SignInUserResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Service
class SignInUserService(
    private val userRepository: UserRepository,
    private val jwtUtils: JWTUtils,
    private val redisRepository: RedisRepository,
) {

    @Transactional(readOnly = true)
    suspend fun signInUser(request: SignInUserRequest): SignInUserResponse {
        with(request) {
            val user =
                userRepository.findByEmailAndSocialLoginType(email, socialLoginType) ?: throw NotFoundUserException()

            val token = jwtUtils.generateUserToken(user.email, user.roles.map { Role(it) }.toList())

            redisRepository.setValue(
                redisRepository.REFRESH_PREFIX + email,
                token.refreshToken,
                jwtUtils.refreshTokenValidityInMilliseconds,
                TimeUnit.MILLISECONDS
            )

            return SignInUserResponse(user, token)
        }
    }
}
