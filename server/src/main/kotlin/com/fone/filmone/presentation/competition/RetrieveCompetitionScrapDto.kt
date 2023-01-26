package com.fone.filmone.presentation.competition

import com.fone.filmone.domain.competition.entity.Competition

class RetrieveCompetitionScrapDto {

    data class RetrieveCompetitionScrapResponse(
        val competitions: List<CompetitionDto>,
    ) {

        constructor(
            competitionList: ArrayList<Competition>,
        ) : this(
            competitions = competitionList.map { CompetitionDto(it) }.toList()
        )
    }
}