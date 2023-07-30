package com.fone.user.domain.service

import com.fone.common.exception.DuplicateUserException
import com.fone.common.exception.InvalidTokenException
import com.fone.user.domain.enum.LoginType
import com.fone.user.domain.repository.UserRepository
import com.fone.user.presentation.dto.SignUpUserDto.EmailSignUpUserRequest
import com.fone.user.presentation.dto.SignUpUserDto.SignUpUserResponse
import com.fone.user.presentation.dto.SignUpUserDto.SocialSignUpUserRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ServerWebInputException

@Service
class SignUpUserService(
    private val userRepository: UserRepository,
    private val oauthValidationService: OauthValidationService,
) {
    @Transactional
    suspend fun signUpUser(request: SocialSignUpUserRequest): SignUpUserResponse {
        with(request) {
            userRepository.findByNicknameOrEmail(nickname, email)?.let {
                throw DuplicateUserException()
            }
            val newUser = toEntity()
            userRepository.save(newUser)
            return SignUpUserResponse(newUser)
        }
    }

    @Transactional
    suspend fun signUpUser(request: EmailSignUpUserRequest): SignUpUserResponse {
        with(request) {
            userRepository.findByNicknameOrEmail(nickname, email)?.let {
                throw DuplicateUserException()
            }

            val newUser = toEntity()
            userRepository.save(newUser)
            return SignUpUserResponse(newUser)
        }
    }

    suspend fun socialLoginValidate(request: SocialSignUpUserRequest) {
        with(request) {
            if (loginType == LoginType.PASSWORD) {
                throw ServerWebInputException("소셜 로그인 타입이 필요합니다.")
            }
            if (!oauthValidationService.isValidTokenSignUp(loginType, accessToken, email, identifier)) {
                throw InvalidTokenException()
            }
        }
    }
}
