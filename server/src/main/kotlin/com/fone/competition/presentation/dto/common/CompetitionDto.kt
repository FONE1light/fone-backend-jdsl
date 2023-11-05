package com.fone.competition.presentation.dto.common

import com.fone.common.utils.DateTimeFormat
import com.fone.competition.domain.entity.Competition
import com.fone.competition.domain.entity.CompetitionScrap
import java.time.LocalDate

data class CompetitionDto(
    val id: Long,
    val title: String,
    val imageUrl: String,
    val screeningStartDate: LocalDate?,
    val screeningEndDate: LocalDate?,
    val exhibitStartDate: LocalDate?,
    val exhibitEndDate: LocalDate?,
    val showStartDate: LocalDate?,
    val agency: String,
    val details: String,
    val viewCount: Long,
    val scrapCount: Long,
    val isScrap: Boolean = false,
    val linkUrl: String,
) {
    val screeningDDay: String
        get() = DateTimeFormat.calculateDays(screeningEndDate)

    val exhibitDDay: String
        get() = DateTimeFormat.calculateDays(exhibitEndDate)

    constructor(
        competition: Competition,
        userCompetitionScrapMap: Map<Long, CompetitionScrap?>,
    ) : this(
        id = competition.id!!,
        title = competition.title,
        imageUrl = competition.imageUrl,
        screeningStartDate = competition.screeningStartDate,
        screeningEndDate = competition.screeningEndDate,
        exhibitStartDate = competition.exhibitStartDate,
        exhibitEndDate = competition.exhibitEndDate,
        showStartDate = competition.showStartDate,
        agency = competition.agency,
        details = competition.details,
        viewCount = competition.viewCount,
        scrapCount = competition.scrapCount,
        isScrap = userCompetitionScrapMap.get(competition.id!!) != null,
        linkUrl = competition.linkUrl
    )
}
