package com.fone.user.application

import com.fone.user.domain.service.OauthValidationService
import com.fone.user.domain.service.SignInUserService
import com.fone.user.presentation.dto.SignInUserDto.EmailSignInUserRequest
import com.fone.user.presentation.dto.SignInUserDto.SignInUserResponse
import com.fone.user.presentation.dto.SignInUserDto.SocialSignInUserRequest
import org.springframework.stereotype.Service

@Service
class SignInUserFacade(
    private val signInUserService: SignInUserService,
    private val oauthValidationService: OauthValidationService,
) {

    suspend fun signIn(request: EmailSignInUserRequest): SignInUserResponse {
        val user = signInUserService.getUser(request)
        signInUserService.validate(request, user)
        return signInUserService.generateResponse(user)
    }

    suspend fun signIn(request: SocialSignInUserRequest): SignInUserResponse {
        val principal = oauthValidationService.getPrincipal(request.loginType, request.accessToken)
        val user = signInUserService.getUser(principal)
        signInUserService.validate(request, user)
        return signInUserService.generateResponse(user)
    }
}
