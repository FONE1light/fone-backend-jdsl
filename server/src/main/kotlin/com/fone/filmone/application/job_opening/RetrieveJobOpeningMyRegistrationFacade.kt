package com.fone.filmone.application.job_opening

import com.fone.filmone.domain.job_opening.service.RetrieveJobOpeningMyRegistrationService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RetrieveJobOpeningMyRegistrationFacade(
    private val retrieveJobOpeningMyRegistrationService: RetrieveJobOpeningMyRegistrationService,
) {

    suspend fun retrieveJobOpeningMyRegistrations(pageable: Pageable, email: String) =
        retrieveJobOpeningMyRegistrationService.retrieveJobOpeningMyRegistrations(pageable, email)
}