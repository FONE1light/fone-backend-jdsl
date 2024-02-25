@file:Suppress("ktlint", "MatchingDeclarationName")

package com.fone.competition.presentation.dto

import com.fone.competition.domain.entity.Competition
import com.fone.competition.domain.entity.CompetitionScrap
import com.fone.competition.presentation.dto.common.CompetitionDto
import org.springframework.data.domain.Page

data class RetrieveCompetitionScrapResponse(
    val competitions: Page<CompetitionDto>,
) {
    constructor(
        competitions: Page<Competition>,
        userCompetitionScrapMap: Map<Long, CompetitionScrap?>,
    ) : this(
        competitions = competitions.map { CompetitionDto(it, userCompetitionScrapMap) }
    )
}
