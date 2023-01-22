package com.fone.filmone.presentation.competition

import com.fone.filmone.domain.competition.entity.Competition
import com.fone.filmone.domain.competition.entity.CompetitionPrize

data class CompetitionDto(
    val id: Long,
    val title: String,
    val imageUrl: String,
    val startDate: String,
    val endDate: String,
    val agency: String,
    val details: String,
    val competitionPrizes: List<CompetitionPrizeDto>,
) {
    constructor(competition: Competition, prizes: List<CompetitionPrize>) : this(
        id = competition.id!!,
        title = competition.title,
        imageUrl = competition.imageUrl,
        startDate = competition.startDate,
        endDate = competition.endDate,
        agency = competition.agency,
        details = competition.details,
        competitionPrizes = prizes.map { CompetitionPrizeDto(it) }.toList(),
    )
}

data class CompetitionPrizeDto(
    val id: Long,
    val ranking: String,
    val prizeMoney: String,
    val agency: String,
    val competitionId: Long,
) {
    constructor(prize: CompetitionPrize) : this(
        id = prize.id!!,
        ranking = prize.ranking,
        prizeMoney = prize.prizeMoney,
        agency = prize.agency,
        competitionId = prize.competitionId,
    )
}