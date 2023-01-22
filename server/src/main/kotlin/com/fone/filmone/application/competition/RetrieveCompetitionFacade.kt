package com.fone.filmone.application.competition

import com.fone.filmone.domain.competition.service.RetrieveCompetitionService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RetrieveCompetitionFacade(
    private val retrieveCompetitionService: RetrieveCompetitionService,
) {

    suspend fun retrieveCompetitions(email: String, pageable: Pageable) =
        retrieveCompetitionService.retrieveCompetition(email, pageable)
}