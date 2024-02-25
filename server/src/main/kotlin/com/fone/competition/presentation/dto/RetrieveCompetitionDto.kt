package com.fone.competition.presentation.dto

import com.fone.competition.domain.entity.Competition
import com.fone.competition.domain.entity.CompetitionScrap
import com.fone.competition.presentation.dto.common.CompetitionDto
import org.springframework.data.domain.Page

data class RetrieveCompetitionsResponse(
    val competitions: Page<CompetitionDto>,
    val totalCount: Long,
) {
    constructor(
        competitions: Page<Competition>,
        userCompetitionScrapMap: Map<Long, CompetitionScrap?>,
        totalCount: Long,
    ) : this(
        competitions = competitions.map {
            CompetitionDto(it, userCompetitionScrapMap)
        },
        totalCount = totalCount
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
