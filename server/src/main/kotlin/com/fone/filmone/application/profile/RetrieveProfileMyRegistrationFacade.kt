package com.fone.filmone.application.profile

import com.fone.filmone.domain.profile.service.RetrieveProfileMyRegistrationService
import org.springframework.stereotype.Service

@Service
class RetrieveProfileMyRegistrationFacade(
    private val retrieveProfileMyRegistrationService: RetrieveProfileMyRegistrationService,
) {

    suspend fun retrieveProfileMyRegistration(email: String) =
        retrieveProfileMyRegistrationService.retrieveProfileMyRegistration(email)
}