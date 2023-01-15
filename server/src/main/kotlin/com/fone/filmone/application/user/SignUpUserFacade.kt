package com.fone.filmone.application.user

import com.fone.filmone.domain.user.service.SignUpUserService
import com.fone.filmone.presentation.user.SignUpUserDto.SignUpUserRequest
import org.springframework.stereotype.Service

@Service
class SignUpUserFacade(
    private val signUpUserService: SignUpUserService,
) {

    suspend fun signUp(request: SignUpUserRequest) = signUpUserService.signUpUser(request)
}