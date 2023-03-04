package com.fone.competition.application

import com.fone.competition.domain.service.ScrapCompetitionService
import org.springframework.stereotype.Service

@Service
class ScrapCompetitionFacade(
    private val scrapCompetitionService: ScrapCompetitionService,
) {

    suspend fun scrapCompetition(email: String, competitionId: Long) =
        scrapCompetitionService.scrapCompetition(email, competitionId)
}
