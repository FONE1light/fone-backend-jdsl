package com.fone.filmone.presentation.competition

import com.fone.filmone.domain.competition.entity.Competition
import com.fone.filmone.domain.competition.entity.CompetitionScrap
import com.fone.filmone.domain.competition.entity.Prize
import java.time.LocalDate

data class CompetitionDto(
    val id: Long,
    val title: String,
    val imageUrl: String,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val agency: String,
    val details: String,
    val viewCount: Long,
    val competitionPrizes: List<CompetitionPrizeDto>,
    val isScrap: Boolean = false,
) {
    constructor(
        competition: Competition,
        userCompetitionScrapMap: Map<Long, CompetitionScrap?>,
    ) : this(
        id = competition.id!!,
        title = competition.title,
        imageUrl = competition.imageUrl,
        startDate = competition.startDate,
        endDate = competition.endDate,
        agency = competition.agency,
        details = competition.details,
        viewCount = competition.viewCount,
        competitionPrizes = competition.prizes.map { CompetitionPrizeDto(it) }.toList(),
        isScrap = userCompetitionScrapMap.get(competition.id!!) != null,
    )
}

data class CompetitionPrizeDto(
    val id: Long,
    val ranking: String,
    val prizeMoney: String,
    val agency: String,
    val competitionId: Long,
) {
    constructor(prize: Prize) : this(
        id = prize.id!!,
        ranking = prize.ranking,
        prizeMoney = prize.prizeMoney,
        agency = prize.agency,
        competitionId = prize.competition!!.id!!,
    )
}