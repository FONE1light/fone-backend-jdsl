package com.fone.filmone.application.job_opening

import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.job_opening.service.RetrieveJobOpeningScrapService
import org.springframework.stereotype.Service

@Service
class RetrieveJobOpeningScrapFacade(
    private val retrieveJobOpeningScrapService: RetrieveJobOpeningScrapService,
) {


    suspend fun retrieveJobOpeningScrap(email: String, type: Type) =
        retrieveJobOpeningScrapService.retrieveJobOpeningScrap(email, type)
}