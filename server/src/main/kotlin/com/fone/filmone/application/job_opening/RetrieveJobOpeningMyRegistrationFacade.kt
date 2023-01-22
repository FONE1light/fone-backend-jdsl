package com.fone.filmone.application.job_opening

import com.fone.filmone.domain.job_opening.service.RetrieveJobOpeningMyRegistrationService
import org.springframework.stereotype.Service

@Service
class RetrieveJobOpeningMyRegistrationFacade(
    private val retrieveJobOpeningMyRegistrationService: RetrieveJobOpeningMyRegistrationService,
) {

    suspend fun retrieveJobOpeningMyRegistrations(email: String) =
        retrieveJobOpeningMyRegistrationService.retrieveJobOpeningMyRegistrations(email)
}