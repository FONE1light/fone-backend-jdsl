package com.fone.competition.presentation.dto

import com.fone.competition.presentation.dto.common.CompetitionDto
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

class RegisterCompetitionDto {

    data class RegisterCompetitionRequest(
        val title: String,
        val imageUrl: String,
        @DateTimeFormat(pattern = "yyyy-MM-dd") val startDate: LocalDate?,
        @DateTimeFormat(pattern = "yyyy-MM-dd") val endDate: LocalDate?,
        @DateTimeFormat(pattern = "yyyy-MM-dd") val submitStartDate: LocalDate?,
        @DateTimeFormat(pattern = "yyyy-MM-dd") val submitEndDate: LocalDate?,
        @DateTimeFormat(pattern = "yyyy-MM-dd") val showStartDate: LocalDate,
        val agency: String,
        val details: String,
        val prizes: List<PrizeRequest>,
    ) {
        fun toEntity(userId: Long): com.fone.competition.domain.entity.Competition {
            return com.fone.competition.domain.entity.Competition(
                title = title,
                imageUrl = imageUrl,
                startDate = startDate,
                endDate = endDate,
                submitStartDate = submitStartDate,
                submitEndDate = submitEndDate,
                showStartDate = showStartDate,
                agency = agency,
                details = details,
                userId = userId,
                viewCount = 0,
                scrapCount = 0
            )
        }
    }

    data class PrizeRequest(
        val ranking: String,
        val prizeMoney: String,
        val competitionId: Long,
    ) {
        fun toEntity(): com.fone.competition.domain.entity.Prize {
            return com.fone.competition.domain.entity.Prize(
                ranking = ranking,
                prizeMoney = prizeMoney
            )
        }
    }

    data class RegisterCompetitionResponse(
        val competition: CompetitionDto,
    ) {
        constructor(
            reqCompetition: com.fone.competition.domain.entity.Competition,
        ) : this(competition = CompetitionDto(reqCompetition, mapOf()))
    }
}
