package com.fone.filmone.application.job_opening

import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.job_opening.service.RetrieveJobOpeningService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RetrieveJobOpeningFacade(
    private val retrieveJobOpeningService: RetrieveJobOpeningService,
) {

    suspend fun retrieveJobOpenings(email: String, pageable: Pageable, type: Type) =
        retrieveJobOpeningService.retrieveJobOpenings(email, pageable, type)

    suspend fun retrieveJobOpening(email: String, type: Type, jobOpeningId: Long) =
        retrieveJobOpeningService.retrieveJobOpening(email, type, jobOpeningId)
}