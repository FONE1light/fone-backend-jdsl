package com.fone.user.application

import com.fone.user.domain.service.SignOutUserService
import org.springframework.stereotype.Service

@Service
class SignOutUserFacade(
    private val signOutUserService: SignOutUserService,
) {

    suspend fun signOutUser(email: String) = signOutUserService.signOutUser(email)
}