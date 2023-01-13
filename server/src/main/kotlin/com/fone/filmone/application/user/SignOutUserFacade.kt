package com.fone.filmone.application.user

import com.fone.filmone.domain.user.service.SignOutUserService
import org.springframework.stereotype.Service

@Service
class SignOutUserFacade(
    private val signOutUserService: SignOutUserService,
) {

    suspend fun signOutUser(email: String) = signOutUserService.signOutUser(email)
}