package com.fone.user.domain.service

import com.fone.common.exception.DuplicateUserException
import com.fone.common.exception.UnauthorizedException
import com.fone.user.domain.enum.LoginType
import com.fone.user.domain.repository.UserRepository
import com.fone.user.presentation.dto.SignUpUserDto.SignUpUserRequest
import com.fone.user.presentation.dto.SignUpUserDto.SignUpUserResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SignUpUserService(
    private val userRepository: UserRepository,
    private val oauthValidationService: OauthValidationService,
) {

    @Transactional
    suspend fun signUpUser(request: SignUpUserRequest): SignUpUserResponse {
        with(request) {
            validate()
            userRepository.findByNicknameOrEmail(nickname, email)?.let {
                throw DuplicateUserException()
            }

            val newUser = toEntity()
            userRepository.save(newUser)
            return SignUpUserResponse(newUser)
        }
    }

    suspend fun SignUpUserRequest.validate() {
        val isValid = when (this.loginType) {
            LoginType.KAKAO, LoginType.APPLE, LoginType.NAVER, LoginType.GOOGLE -> {
                oauthValidationService.isValidTokenSignIn(loginType, accessToken!!, email)
            }

            LoginType.PASSWORD -> {
                this.password != null
            }
        }
        if (!isValid) {
            throw UnauthorizedException(HttpStatus.UNAUTHORIZED, "유효하지 않은 회원가입 시도입니다.")
        }
    }
}
