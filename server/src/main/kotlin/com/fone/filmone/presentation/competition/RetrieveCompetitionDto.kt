package com.fone.filmone.presentation.competition

import org.springframework.data.domain.Slice

class RetrieveCompetitionDto {

    data class RetrieveCompetitionsResponse(
        val competitions: Slice<CompetitionDto>,
    )

    data class RetrieveCompetitionResponse(
        val competition: CompetitionDto,
    )
}