package com.fone.user.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.common.exception.UnauthorizedException
import com.fone.common.jwt.JWTUtils
import com.fone.common.jwt.Role
import com.fone.common.password.PasswordService
import com.fone.common.redis.RedisRepository
import com.fone.user.domain.entity.User
import com.fone.user.domain.enum.LoginType
import com.fone.user.domain.repository.UserRepository
import com.fone.user.presentation.dto.SignInUserDto.SignInUserRequest
import com.fone.user.presentation.dto.SignInUserDto.SignInUserResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Service
class SignInUserService(
    private val userRepository: UserRepository,
    private val jwtUtils: JWTUtils,
    private val redisRepository: RedisRepository,
    private val oauthValidationService: OauthValidationService,
) {

    @Transactional(readOnly = true)
    suspend fun signInUser(request: SignInUserRequest): SignInUserResponse {
        with(request) {
            val user =
                userRepository.findByEmailAndLoginType(email, loginType) ?: throw NotFoundUserException()
            validate(user)
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

    suspend fun SignInUserRequest.validate(user: User) {
        val isValid = when (user.loginType) {
            LoginType.KAKAO, LoginType.APPLE, LoginType.NAVER, LoginType.GOOGLE -> {
                oauthValidationService.isValidTokenSignIn(loginType, accessToken!!, email)
            }
            LoginType.PASSWORD -> {
                this.password != null &&
                    user.password != null &&
                    PasswordService.isValidPassword(this.password, user.password)
            }
        }
        if (!isValid) {
            throw UnauthorizedException(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증시도입니다.")
        }
    }
}
