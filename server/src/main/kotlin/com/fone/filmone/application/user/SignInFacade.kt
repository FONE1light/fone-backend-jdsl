package com.fone.filmone.application.user

import com.fone.filmone.domain.user.service.RetrieveUserService
import com.fone.filmone.presentation.user.SignInDto
import org.springframework.stereotype.Service

@Service
class SignInFacade(
    private val retrieveUserService: RetrieveUserService,
) {

    suspend fun signIn(request: SignInDto.SignInRequest) = retrieveUserService.retrieveUser(request)
}