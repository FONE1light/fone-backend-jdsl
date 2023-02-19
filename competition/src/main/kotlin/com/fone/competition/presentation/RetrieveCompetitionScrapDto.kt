package com.fone.competition.presentation

import com.fone.competition.domain.entity.Competition
import com.fone.competition.domain.entity.CompetitionScrap
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