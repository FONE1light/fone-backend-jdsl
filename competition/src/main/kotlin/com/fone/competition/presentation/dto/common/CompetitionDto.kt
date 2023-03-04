package com.fone.competition.presentation.dto.common

import com.fone.common.utils.DateTimeFormat
import com.fone.competition.domain.entity.Competition
import com.fone.competition.domain.entity.CompetitionScrap
import com.fone.competition.domain.entity.Prize
import java.time.LocalDate

data class CompetitionDto(
    val id: Long,
    val title: String,
    val imageUrl: String,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val submitStartDate: LocalDate?,
    val submitEndDate: LocalDate?,
    val showStartDate: LocalDate?,
    val agency: String,
    val details: String,
    val viewCount: Long,
    val scrapCount: Long,
    val competitionPrizes: List<CompetitionPrizeDto>,
    val isScrap: Boolean = false,
    val dDay: String
) {
    constructor(
        competition: Competition,
        userCompetitionScrapMap: Map<Long, CompetitionScrap?>
    ) : this(
        id = competition.id!!,
        title = competition.title,
        imageUrl = competition.imageUrl,
        startDate = competition.startDate,
        endDate = competition.endDate,
        submitStartDate = competition.submitStartDate,
        submitEndDate = competition.submitEndDate,
        showStartDate = competition.showStartDate,
        agency = competition.agency,
        details = competition.details,
        viewCount = competition.viewCount,
        scrapCount = competition.scrapCount,
        competitionPrizes = competition.prizes.map { CompetitionPrizeDto(it) }.toList(),
        isScrap = userCompetitionScrapMap.get(competition.id!!) != null,
        dDay = DateTimeFormat.calculateDays(competition.endDate ?: competition.submitEndDate)
    )
}

data class CompetitionPrizeDto(
    val id: Long,
    val ranking: String,
    val prizeMoney: String,
    val competitionId: Long
) {
    constructor(
        prize: Prize
    ) : this(
        id = prize.id!!,
        ranking = prize.ranking,
        prizeMoney = prize.prizeMoney,
        competitionId = prize.competition!!.id!!
    )
}
