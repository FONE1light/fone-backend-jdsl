package com.fone.user.application

import com.fone.user.domain.service.SignUpUserService
import com.fone.user.presentation.dto.SignUpUserDto.SignUpUserRequest
import org.springframework.stereotype.Service

@Service
class SignUpUserFacade(
    private val signUpUserService: SignUpUserService,
) {

    suspend fun signUp(request: SignUpUserRequest) = signUpUserService.signUpUser(request)
}
