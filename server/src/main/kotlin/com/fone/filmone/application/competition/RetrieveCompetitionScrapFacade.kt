package com.fone.filmone.application.competition

import com.fone.filmone.domain.competition.service.RetrieveCompetitionScrapService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RetrieveCompetitionScrapFacade(
    private val retrieveCompetitionScrapService: RetrieveCompetitionScrapService,
) {

    suspend fun retrieveCompetitionScraps(pageable: Pageable, email: String) =
        retrieveCompetitionScrapService.retrieveCompetitionScraps(pageable, email)
}