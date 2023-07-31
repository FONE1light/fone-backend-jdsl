package com.fone.user.application

import com.fone.user.domain.service.SignUpUserService
import com.fone.user.presentation.dto.SignUpUserDto.EmailSignUpUserRequest
import com.fone.user.presentation.dto.SignUpUserDto.SignUpUserResponse
import com.fone.user.presentation.dto.SignUpUserDto.SocialSignUpUserRequest
import org.springframework.stereotype.Service

@Service
class SignUpUserFacade(
    private val signUpUserService: SignUpUserService,
) {

    suspend fun signUp(request: SocialSignUpUserRequest): SignUpUserResponse {
        signUpUserService.socialLoginValidate(request)
        return signUpUserService.signUpUser(request)
    }
    suspend fun signUp(request: EmailSignUpUserRequest): SignUpUserResponse {
        signUpUserService.emailLoginValidate(request)
        return signUpUserService.signUpUser(request)
    }
}
