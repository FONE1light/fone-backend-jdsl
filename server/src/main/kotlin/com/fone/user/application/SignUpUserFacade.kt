package com.fone.user.application

import com.fone.common.jwt.Role
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
