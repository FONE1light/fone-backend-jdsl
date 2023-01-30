package com.fone.filmone.application.job_opening

import com.fone.filmone.domain.job_opening.service.RetrieveMySimilarJobOpeningService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RetrieveMySimilarJobOpeningFacade(
    private val retrieveMySimilarJobOpeningService: RetrieveMySimilarJobOpeningService
) {

    suspend fun retrieveMySimilarJobOpening(pageable: Pageable, email: String) =
        retrieveMySimilarJobOpeningService.retrieveMySimilarJobOpening(pageable, email)
}