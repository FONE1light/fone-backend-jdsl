package com.fone.filmone.application.job_opening

import com.fone.common.entity.Type
import com.fone.filmone.domain.job_opening.service.RetrieveJobOpeningScrapService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RetrieveJobOpeningScrapFacade(
    private val retrieveJobOpeningScrapService: RetrieveJobOpeningScrapService,
) {


    suspend fun retrieveJobOpeningScrap(pageable: Pageable, email: String, type: Type) =
        retrieveJobOpeningScrapService.retrieveJobOpeningScrap(pageable, email, type)
}