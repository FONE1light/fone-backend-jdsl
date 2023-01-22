package com.fone.filmone.application.competition

import com.fone.filmone.domain.competition.service.ScrapCompetitionService
import org.springframework.stereotype.Service

@Service
class ScrapCompetitionFacade(
    private val scrapCompetitionService: ScrapCompetitionService,
) {

    suspend fun scrapCompetition(email: String, competitionId: Long) =
        scrapCompetitionService.scrapCompetition(email, competitionId)
}