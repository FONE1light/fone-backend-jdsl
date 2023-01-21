package com.fone.filmone.application.job_opening

import com.fone.filmone.domain.job_opening.service.ScrapJobOpeningService
import org.springframework.stereotype.Service

@Service
class ScrapJobOpeningFacade(
    private val scrapJobOpeningService: ScrapJobOpeningService,
) {

    suspend fun scrapJobOpening(email: String, jobOpeningId: Long) =
        scrapJobOpeningService.scrapJobOpening(email, jobOpeningId)
}