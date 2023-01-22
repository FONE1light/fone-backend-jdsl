package com.fone.filmone.application.competition

import com.fone.filmone.domain.competition.service.RetrieveCompetitionScrapService
import org.springframework.stereotype.Service

@Service
class RetrieveCompetitionScrapFacade(
    private val retrieveCompetitionScrapService: RetrieveCompetitionScrapService,
) {

    suspend fun retrieveCompetitionScraps(email: String) =
        retrieveCompetitionScrapService.retrieveCompetitionScraps(email)
}