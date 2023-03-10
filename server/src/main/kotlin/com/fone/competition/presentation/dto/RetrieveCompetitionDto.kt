package com.fone.competition.presentation.dto

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
            competitions: List<com.fone.competition.domain.entity.Competition>,
            userCompetitionScrapMap: Map<Long, com.fone.competition.domain.entity.CompetitionScrap?>,
            totalCount: Long,
            pageable: Pageable,
        ) : this(
            competitions = PageImpl(
                competitions.map { CompetitionDto(it, userCompetitionScrapMap) }.toList(),
                pageable,
                competitions.size.toLong()
            ),
            totalCount = totalCount
        )
    }

    data class RetrieveCompetitionResponse(
        val competition: CompetitionDto,
    ) {
        constructor(
            reqCompetition: com.fone.competition.domain.entity.Competition,
            userCompetitionScrapMap: Map<Long, com.fone.competition.domain.entity.CompetitionScrap?>,
        ) : this(competition = CompetitionDto(reqCompetition, userCompetitionScrapMap))
    }
}
