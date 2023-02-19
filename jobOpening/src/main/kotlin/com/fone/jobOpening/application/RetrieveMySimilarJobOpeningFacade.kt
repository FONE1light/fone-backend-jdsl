package com.fone.jobOpening.application

import com.fone.jobOpening.domain.service.RetrieveMySimilarJobOpeningService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RetrieveMySimilarJobOpeningFacade(
    private val retrieveMySimilarJobOpeningService: RetrieveMySimilarJobOpeningService,
) {

    suspend fun retrieveMySimilarJobOpening(pageable: Pageable, email: String) =
        retrieveMySimilarJobOpeningService.retrieveMySimilarJobOpening(pageable, email)
}