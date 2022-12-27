package com.fone.filmone.application.auth

import com.fone.filmone.domain.user.service.UserRetrieveService
import com.fone.filmone.presentation.auth.SignInDto
import org.springframework.stereotype.Service

@Service
class SignInFacade(
    private val retrieveUserService: UserRetrieveService,
) {

    suspend fun signIn(request: SignInDto.SignInRequest) = retrieveUserService.retrieveUser(request)
}