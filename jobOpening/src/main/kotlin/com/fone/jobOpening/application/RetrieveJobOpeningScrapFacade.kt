package com.fone.jobOpening.application

import com.fone.common.entity.Type
import com.fone.jobOpening.domain.service.RetrieveJobOpeningScrapService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RetrieveJobOpeningScrapFacade(
    private val retrieveJobOpeningScrapService: RetrieveJobOpeningScrapService,
) {

    suspend fun retrieveJobOpeningScrap(pageable: Pageable, email: String, type: Type) =
        retrieveJobOpeningScrapService.retrieveJobOpeningScrap(pageable, email, type)
}
