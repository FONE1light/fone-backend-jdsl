package com.fone.jobOpening.application

import com.fone.jobOpening.domain.service.ScrapJobOpeningService
import com.fone.jobOpening.presentation.dto.ScrapJobOpeningResponse
import org.springframework.stereotype.Service

@Service
class ScrapJobOpeningFacade(
    private val scrapJobOpeningService: ScrapJobOpeningService,
) {

    suspend fun scrapJobOpening(email: String, jobOpeningId: Long): ScrapJobOpeningResponse =
        scrapJobOpeningService.scrapJobOpening(email, jobOpeningId)
}
