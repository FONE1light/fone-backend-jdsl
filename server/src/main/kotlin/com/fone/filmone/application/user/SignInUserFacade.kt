package com.fone.filmone.application.user

import com.fone.filmone.domain.user.service.SignInUserService
import com.fone.filmone.presentation.user.SignInUserDto
import org.springframework.stereotype.Service

@Service
class SignInUserFacade(
    private val signInUserService: SignInUserService,
) {

    suspend fun signIn(request: SignInUserDto.SignInUserRequest) = signInUserService.signInUser(request)
}