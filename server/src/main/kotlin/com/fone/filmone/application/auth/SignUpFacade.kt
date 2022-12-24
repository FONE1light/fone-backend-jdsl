package com.fone.filmone.application.auth

import com.fone.filmone.domain.user.service.RegisterUserService
import com.fone.filmone.presentation.auth.SignUpDto.SignUpRequest
import org.springframework.stereotype.Service

@Service
class SignUpFacade(
    private val registerUserService: RegisterUserService,
) {

    suspend fun signUp(request: SignUpRequest) = registerUserService.registerUser(request)
}