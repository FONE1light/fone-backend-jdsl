package com.fone.user.application

import com.fone.user.domain.service.SignInUserService
import com.fone.user.presentation.SignInUserDto.SignInUserRequest
import org.springframework.stereotype.Service

@Service
class SignInUserFacade(
    private val signInUserService: SignInUserService,
) {

    suspend fun signIn(request: SignInUserRequest) = signInUserService.signInUser(request)
}