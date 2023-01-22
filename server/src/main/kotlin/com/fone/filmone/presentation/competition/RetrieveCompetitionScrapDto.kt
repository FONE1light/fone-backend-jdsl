package com.fone.filmone.presentation.competition

import com.fone.filmone.domain.competition.entity.Competition
import com.fone.filmone.domain.competition.entity.CompetitionPrize

class RetrieveCompetitionScrapDto {

    data class RetrieveCompetitionScrapResponse(
        val competitions: List<CompetitionDto>,
    ) {

        constructor(
            competitionList: List<Competition>,
            prizes: List<List<CompetitionPrize>>,
        ) : this(
            competitions = competitionList.zip(prizes) { c, p -> CompetitionDto(c, p) }.toList()
        )
    }
}