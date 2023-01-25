package com.fone.filmone.application.profile

import com.fone.filmone.domain.profile.service.RetrieveProfileMyRegistrationService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RetrieveProfileMyRegistrationFacade(
    private val retrieveProfileMyRegistrationService: RetrieveProfileMyRegistrationService,
) {

    suspend fun retrieveProfileMyRegistration(pageable: Pageable, email: String) =
        retrieveProfileMyRegistrationService.retrieveProfileMyRegistration(pageable, email)
}