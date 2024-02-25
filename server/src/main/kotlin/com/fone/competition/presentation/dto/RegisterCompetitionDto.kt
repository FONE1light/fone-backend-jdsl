package com.fone.competition.presentation.dto

import com.fone.competition.domain.entity.Competition
import com.fone.competition.presentation.dto.common.CompetitionDto
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class RegisterCompetitionRequest(
    @Schema(description = "제목", example = "제목")
    val title: String,
    @Schema(description = "포스터 사진 URL", example = "https://www.naver.com")
    val imageUrl: String,
    @Schema(description = "상영기간 시작일", example = "2021-10-10")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val screeningStartDate: LocalDate?,
    @Schema(description = "상영기간 종료일", example = "2021-10-10")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val screeningEndDate: LocalDate?,
    @Schema(description = "출품기간 시작일", example = "2021-10-10")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val exhibitStartDate: LocalDate?,
    @Schema(description = "출품기간 종료일", example = "2021-10-10")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val exhibitEndDate: LocalDate?,
    @Schema(description = "영화제 보여주는 시작일", example = "2021-10-10")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val showStartDate: LocalDate,
    @Schema(description = "주최", example = "방송통신 위원회")
    val agency: String,
    @Schema(description = "소개", example = "#참가자격...")
    val details: String,
    @Schema(description = "보러가기 링크 RUL", example = "https://www.naver.com")
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
