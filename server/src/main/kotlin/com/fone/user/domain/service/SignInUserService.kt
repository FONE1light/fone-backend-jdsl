package com.fone.user.domain.service

import com.fone.common.exception.InvalidOauthStatusException
import com.fone.common.exception.InvalidTokenException
import com.fone.common.exception.NotFoundUserException
import com.fone.common.jwt.JWTUtils
import com.fone.common.jwt.Role
import com.fone.common.jwt.Token
import com.fone.common.password.PasswordService
import com.fone.common.redis.RedisRepository
import com.fone.user.domain.entity.OauthPrincipal
import com.fone.user.domain.entity.User
import com.fone.user.domain.enum.LoginType
import com.fone.user.domain.repository.UserRepository
import com.fone.user.presentation.dto.SignInUserDto.EmailSignInUserRequest
import com.fone.user.presentation.dto.SignInUserDto.SignInUserResponse
import com.fone.user.presentation.dto.SignInUserDto.SocialSignInUserRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ServerWebInputException
import java.util.concurrent.TimeUnit

@Service
class SignInUserService(
    private val userRepository: UserRepository,
    private val jwtUtils: JWTUtils,
    private val redisRepository: RedisRepository,
) {

    @Transactional(readOnly = true)
    suspend fun getUser(request: EmailSignInUserRequest): User {
        with(request) {
            return userRepository.findByEmailAndLoginType(email, LoginType.PASSWORD) ?: throw NotFoundUserException()
        }
    }

    @Transactional(readOnly = true)
    suspend fun getUser(principal: OauthPrincipal): User {
        return when (principal.type) {
            LoginType.APPLE -> {
                if (principal.identifier == null) {
                    throw InvalidOauthStatusException("Apple 로그인에는 identifier가 필수입니다.")
                }
                userRepository.findByIdentifier(principal.identifier)
            }

            else -> {
                userRepository.findByEmailAndLoginType(principal.email, principal.type)
            }
        } ?: throw NotFoundUserException()
    }

    suspend fun signInUser(token: Token) {
        val email = jwtUtils.getEmailFromToken(token.refreshToken)
        val loginUser = userRepository.findByEmail(email)
        if (loginUser != null) {
            loginUser.login()
            userRepository.save(loginUser)
        }
    }

    suspend fun signInUser(user: User) {
        user.login()
        userRepository.save(user)
    }

    suspend fun validate(request: SocialSignInUserRequest, user: User) {
        with(request) {
            if (loginType == LoginType.PASSWORD) {
                throw ServerWebInputException("소셜 로그인 타입이 필요합니다.")
            }
        }
    }

    suspend fun validate(request: EmailSignInUserRequest, user: User) {
        with(request) {
            val isValid = user.password != null &&
                PasswordService.isValidPassword(this.password, user.password!!)
            if (!isValid) {
                throw InvalidTokenException("유효하지 않은 인증시도입니다.")
            }
        }
    }

    fun generateResponse(user: User): SignInUserResponse {
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
