package com.fone.competition.presentation.dto

import com.fone.competition.domain.entity.Competition
import com.fone.competition.domain.entity.CompetitionScrap
import com.fone.competition.presentation.dto.common.CompetitionDto
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

class RetrieveCompetitionDto {

    data class RetrieveCompetitionsResponse(
        val competitions: Slice<CompetitionDto>,
        val totalCount: Long,
    ) {
        constructor(
            competitions: List<Competition>,
            userCompetitionScrapMap: Map<Long, CompetitionScrap?>,
            totalCount: Long,
            pageable: Pageable,
        ) : this(
            competitions =
                PageImpl(
                    competitions.map { CompetitionDto(it, userCompetitionScrapMap) }.toList(),
                    pageable,
                    competitions.size.toLong()
                ),
            totalCount = totalCount,
        )
    }

    data class RetrieveCompetitionResponse(
        val competition: CompetitionDto,
    ) {
        constructor(
            reqCompetition: Competition,
            userCompetitionScrapMap: Map<Long, CompetitionScrap?>,
        ) : this(competition = CompetitionDto(reqCompetition, userCompetitionScrapMap))
    }
}
