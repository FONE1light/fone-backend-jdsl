package com.fone.user.application

import com.fone.user.domain.service.SignUpUserService
import com.fone.user.presentation.dto.EmailSignUpUserRequest
import com.fone.user.presentation.dto.SignUpUserResponse
import com.fone.user.presentation.dto.SocialSignUpUserRequest
import org.springframework.stereotype.Service

@Service
class SignUpUserFacade(
    private val signUpUserService: SignUpUserService,
) {

    suspend fun signUp(request: SocialSignUpUserRequest): SignUpUserResponse {
        signUpUserService.socialLoginValidate(request)
        val signUpUser = signUpUserService.signUpUser(request)
        val token = signUpUserService.getToken(signUpUser)
        return SignUpUserResponse(signUpUser, token)
    }
    suspend fun signUp(request: EmailSignUpUserRequest): SignUpUserResponse {
        signUpUserService.emailLoginValidate(request)
        val signUpUser = signUpUserService.signUpUser(request)
        val token = signUpUserService.getToken(signUpUser)
        return SignUpUserResponse(signUpUser, token)
    }
}
