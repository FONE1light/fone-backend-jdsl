package com.fone.user.domain.service

import com.fone.common.exception.InvalidTokenException
import com.fone.common.exception.NotFoundUserException
import com.fone.common.jwt.JWTUtils
import com.fone.common.jwt.Role
import com.fone.common.password.PasswordService
import com.fone.common.redis.RedisRepository
import com.fone.user.domain.entity.User
import com.fone.user.domain.enum.LoginType
import com.fone.user.domain.repository.UserRepository
import com.fone.user.presentation.dto.SignInUserDto.EmailSignInUserRequest
import com.fone.user.presentation.dto.SignInUserDto.SignInUserResponse
import com.fone.user.presentation.dto.SignInUserDto.SocialSignInUserRequest
import org.springframework.stereotype.Service
import org.springframework.web.server.ServerWebInputException
import java.util.concurrent.TimeUnit

@Service
class SignInUserService(
    private val userRepository: UserRepository,
    private val jwtUtils: JWTUtils,
    private val redisRepository: RedisRepository,
    private val oauthValidationService: OauthValidationService,
) {

    suspend fun signInUser(request: EmailSignInUserRequest): SignInUserResponse {
        with(request) {
            val user =
                userRepository.findByEmailAndLoginType(email, LoginType.PASSWORD) ?: throw NotFoundUserException()
            validate(user)
            return generateResponse(user)
        }
    }

    suspend fun signInUser(request: SocialSignInUserRequest): SignInUserResponse {
        with(request) {
            val email = oauthValidationService.getEmail(request)
            val user =
                userRepository.findByEmailAndLoginType(email, loginType) ?: throw NotFoundUserException()
            validate(user)
            return generateResponse(user)
        }
    }

    private suspend fun SocialSignInUserRequest.validate(user: User) {
        if (loginType == LoginType.PASSWORD) {
            throw ServerWebInputException("소셜 로그인 타입이 필요합니다.")
        }
        if (!oauthValidationService.isValidTokenSignIn(loginType, accessToken, user.email)) {
            throw InvalidTokenException()
        }
    }

    private suspend fun EmailSignInUserRequest.validate(user: User) {
        val isValid = user.password != null &&
            PasswordService.isValidPassword(this.password, user.password!!)
        if (!isValid) {
            throw InvalidTokenException("유효하지 않은 인증시도입니다.")
        }
    }

    private fun generateResponse(user: User): SignInUserResponse {
        val token = jwtUtils.generateUserToken(user.email, user.roles.map { Role(it) }.toList())

        redisRepository.setValue(
            redisRepository.REFRESH_PREFIX + user.email,
            token.refreshToken,
            jwtUtils.refreshTokenValidityInMilliseconds,
            TimeUnit.MILLISECONDS
        )

        return SignInUserResponse(user, token)
    }
}
