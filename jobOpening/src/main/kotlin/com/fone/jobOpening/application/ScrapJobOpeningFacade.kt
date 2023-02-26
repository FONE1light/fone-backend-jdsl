package com.fone.jobOpening.application

import com.fone.jobOpening.domain.service.ScrapJobOpeningService
import org.springframework.stereotype.Service

@Service
class ScrapJobOpeningFacade(
    private val scrapJobOpeningService: ScrapJobOpeningService,
) {

    suspend fun scrapJobOpening(email: String, jobOpeningId: Long) =
        scrapJobOpeningService.scrapJobOpening(email, jobOpeningId)
}
