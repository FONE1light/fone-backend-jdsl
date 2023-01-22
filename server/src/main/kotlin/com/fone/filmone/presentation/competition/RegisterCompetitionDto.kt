package com.fone.filmone.presentation.competition

import com.fone.filmone.domain.competition.entity.Competition
import com.fone.filmone.domain.competition.entity.CompetitionPrize
import org.springframework.format.annotation.DateTimeFormat
import java.sql.Date

class RegisterCompetitionDto {

    data class RegisterCompetitionRequest(
        val title: String,
        val imageUrl: String,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        val startDate: Date,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        val endDate: Date,
        val agency: String,
        val details: String,
        val prizes: List<CompetitionPrizeRequest>,
    ) {
        fun toEntity(userId: Long): Competition {
            return Competition(
                title = title,
                imageUrl = imageUrl,
                startDate = startDate.toString(),
                endDate = endDate.toString(),
                agency = agency,
                details = details,
                userId = userId,
                viewCount = 0,
            )
        }
    }

    data class CompetitionPrizeRequest(
        val ranking: String,
        val prizeMoney: String,
        val agency: String,
        val competitionId: Long,
    ) {
        fun toEntity(competitionId: Long): CompetitionPrize {
            return CompetitionPrize(
                ranking = ranking,
                prizeMoney = prizeMoney,
                agency = agency,
                competitionId = competitionId,
            )
        }
    }

    data class RegisterCompetitionResponse(
        val competition: CompetitionDto,
    ) {
        constructor(competition: Competition, prizes: List<CompetitionPrize>) : this(
            competition = CompetitionDto(competition, prizes)
        )
    }
}