package com.fone.competition.presentation.dto

import com.fone.competition.domain.entity.Competition
import com.fone.competition.presentation.dto.common.CompetitionDto
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

class RegisterCompetitionDto {

    data class RegisterCompetitionRequest(
        val title: String,
        val imageUrl: String,
        @DateTimeFormat(pattern = "yyyy-MM-dd") val screeningStartDate: LocalDate?,
        @DateTimeFormat(pattern = "yyyy-MM-dd") val screeningEndDate: LocalDate?,
        @DateTimeFormat(pattern = "yyyy-MM-dd") val exhibitStartDate: LocalDate?,
        @DateTimeFormat(pattern = "yyyy-MM-dd") val exhibitEndDate: LocalDate?,
        @DateTimeFormat(pattern = "yyyy-MM-dd") val showStartDate: LocalDate,
        val agency: String,
        val details: String,
        val linkUrl: String,
    ) {
        fun toEntity(userId: Long): Competition {
            return Competition(
                title = title,
                imageUrl = imageUrl,
                screeningStartDate = screeningStartDate,
                screeningEndDate = screeningEndDate,
                exhibitStartDate = exhibitStartDate,
                exhibitEndDate = exhibitEndDate,
                showStartDate = showStartDate,
                agency = agency,
                details = details,
                userId = userId,
                viewCount = 0,
                scrapCount = 0,
                linkUrl = linkUrl
            )
        }
    }

    data class RegisterCompetitionResponse(
        val competition: CompetitionDto,
    ) {
        constructor(
            reqCompetition: Competition,
        ) : this(competition = CompetitionDto(reqCompetition, mapOf()))
    }
}
