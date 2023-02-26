package com.fone.jobOpening.application

import com.fone.jobOpening.domain.service.RetrieveJobOpeningMyRegistrationService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RetrieveJobOpeningMyRegistrationFacade(
    private val retrieveJobOpeningMyRegistrationService: RetrieveJobOpeningMyRegistrationService,
) {

    suspend fun retrieveJobOpeningMyRegistrations(pageable: Pageable, email: String) =
        retrieveJobOpeningMyRegistrationService.retrieveJobOpeningMyRegistrations(pageable, email)
}
