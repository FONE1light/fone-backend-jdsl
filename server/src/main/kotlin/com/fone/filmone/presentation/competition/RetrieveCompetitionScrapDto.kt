package com.fone.filmone.presentation.competition

import org.springframework.data.domain.Slice

class RetrieveCompetitionScrapDto {

    data class RetrieveCompetitionScrapResponse(
        val competitions: Slice<CompetitionDto>,
    )
}