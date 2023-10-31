package com.fone.competition.presentation.dto.common

import com.fone.common.utils.DateTimeFormat
import com.fone.competition.domain.entity.Competition
import com.fone.competition.domain.entity.CompetitionScrap
import java.time.LocalDate

data class CompetitionDto(
    val id: Long,
    val title: String,
    val imageUrl: String,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val showStartDate: LocalDate?,
    val agency: String,
    val details: String,
    val viewCount: Long,
    val scrapCount: Long,
    val isScrap: Boolean = false,
) {
    val dDay: String
        get() = DateTimeFormat.calculateDays(endDate)

    constructor(
        competition: Competition,
        userCompetitionScrapMap: Map<Long, CompetitionScrap?>,
    ) : this(
        id = competition.id!!,
        title = competition.title,
        imageUrl = competition.imageUrl,
        startDate = competition.startDate,
        endDate = competition.endDate,
        showStartDate = competition.showStartDate,
        agency = competition.agency,
        details = competition.details,
        viewCount = competition.viewCount,
        scrapCount = competition.scrapCount,
        isScrap = userCompetitionScrapMap.get(competition.id!!) != null
    )
}
