package com.fone.user.domain.service

import com.fone.common.exception.DuplicatePhoneNumberException
import com.fone.common.exception.DuplicateUserException
import com.fone.common.exception.InvalidTokenException
import com.fone.common.jwt.JWTUtils
import com.fone.common.jwt.Role
import com.fone.common.jwt.Token
import com.fone.common.redis.RedisRepository
import com.fone.user.application.EmailValidationFacade
import com.fone.user.application.EmailValidationFacadeNoOp
import com.fone.user.domain.entity.User
import com.fone.user.domain.enum.LoginType
import com.fone.user.domain.repository.UserRepository
import com.fone.user.presentation.dto.EmailSignUpUserRequest
import com.fone.user.presentation.dto.SocialSignUpUserRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ServerWebInputException
import java.util.concurrent.TimeUnit

@Service
class SignUpUserService(
    private val userRepository: UserRepository,
    private val oauthValidationService: OauthValidationService,
    private val redisRepository: RedisRepository,
    private val emailValidationFacade: EmailValidationFacade,
    private val jwtUtils: JWTUtils,
) {
    @Transactional
    suspend fun signUpUser(request: SocialSignUpUserRequest): User {
        with(request) {
            userRepository.findByNicknameOrEmail(nickname, email)?.let {
                throw DuplicateUserException()
            }

            userRepository.findByPhone(request.phoneNumber)?.let {
                throw DuplicatePhoneNumberException()
            }

            val newUser = toEntity()
            userRepository.save(newUser)
            return newUser
        }
    }

    @Transactional
    suspend fun signUpUser(request: EmailSignUpUserRequest): User {
        with(request) {
            userRepository.findByNicknameOrEmail(nickname, email)?.let {
                throw DuplicateUserException()
            }

            userRepository.findByPhone(request.phoneNumber)?.let {
                throw DuplicatePhoneNumberException()
            }

            val newUser = toEntity()
            userRepository.save(newUser)
            return newUser
        }
    }

    suspend fun getToken(user: User): Token {
        val token = jwtUtils.generateUserToken(user.email, user.roles.map { Role(it) }.toList())

        redisRepository.setValue(
            redisRepository.REFRESH_PREFIX + user.email,
            token.refreshToken,
            jwtUtils.refreshTokenValidityInMilliseconds,
            TimeUnit.MILLISECONDS
        )

        return token
    }

    suspend fun socialLoginValidate(request: SocialSignUpUserRequest) {
        with(request) {
            if (loginType == LoginType.PASSWORD) {
                throw ServerWebInputException("소셜 로그인 타입이 필요합니다.")
            }
            if (email == null) {
                email = oauthValidationService.getPrincipal(request.loginType, request.accessToken).email
            }
            if (!oauthValidationService.isValidTokenSignUp(loginType, accessToken, email!!, identifier)) {
                throw InvalidTokenException()
            }
        }
    }

    suspend fun emailLoginValidate(request: EmailSignUpUserRequest) {
        if (emailValidationFacade is EmailValidationFacadeNoOp) return
        with(request) {
            if (token != redisRepository.getValue("user:$email:emailSignUpToken")) {
                throw InvalidTokenException()
            }
            redisRepository.delValue("user:$email:emailToken")
        }
    }
}
