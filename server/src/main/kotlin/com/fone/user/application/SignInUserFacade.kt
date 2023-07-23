package com.fone.user.application

import com.fone.user.domain.service.SignInUserService
import com.fone.user.presentation.dto.SignInUserDto.PasswordSignInUserRequest
import com.fone.user.presentation.dto.SignInUserDto.SocialSignInUserRequest
import org.springframework.stereotype.Service

@Service
class SignInUserFacade(
    private val signInUserService: SignInUserService,
) {

    suspend fun signIn(request: PasswordSignInUserRequest) = signInUserService.signInUser(request)
    suspend fun signIn(request: SocialSignInUserRequest) = signInUserService.signInUser(request)
}
