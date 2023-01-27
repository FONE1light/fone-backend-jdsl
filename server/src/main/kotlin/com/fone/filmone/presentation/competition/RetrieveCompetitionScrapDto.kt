package com.fone.filmone.presentation.competition

import com.fone.filmone.domain.competition.entity.Competition
import com.fone.filmone.domain.competition.entity.CompetitionScrap
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

class RetrieveCompetitionScrapDto {

    data class RetrieveCompetitionScrapResponse(
        val competitions: Slice<CompetitionDto>,
    ) {
        constructor(
            competitions: List<Competition>,
            userCompetitionScrapMap: Map<Long, CompetitionScrap?>,
            pageable: Pageable,
        ) : this(
            competitions = PageImpl(
                competitions.map { CompetitionDto(it, userCompetitionScrapMap) }.toList(),
                pageable,
                competitions.size.toLong()
            )
        )
    }
}