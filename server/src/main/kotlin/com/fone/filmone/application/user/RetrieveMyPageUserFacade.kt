package com.fone.filmone.application.user

import com.fone.filmone.domain.user.service.RetrieveUserService
import org.springframework.stereotype.Service

@Service
class RetrieveMyPageUserFacade(
    private val retrieveUserService: RetrieveUserService,
) {

    suspend fun retrieveMyPageUser(email: String) = retrieveUserService.retrieveUser(email)
}