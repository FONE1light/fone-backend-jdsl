package com.fone.user.application

import com.fone.user.domain.service.RetrieveUserService
import org.springframework.stereotype.Service

@Service
class RetrieveMyPageUserFacade(
    private val retrieveUserService: RetrieveUserService,
) {

    suspend fun retrieveMyPageUser(email: String) = retrieveUserService.retrieveUser(email)
}