package com.fone.competition.presentation.dto.common

import com.fone.common.utils.DateTimeFormat
import com.fone.competition.domain.entity.Competition
import com.fone.competition.domain.entity.CompetitionScrap
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class CompetitionDto(
    @Schema(description = "영화제 ID", example = "1")
    val id: Long,
    @Schema(description = "제목", example = "제목")
    val title: String,
    @Schema(description = "포스터 사진 URL", example = "https://www.naver.com")
    val imageUrl: String,
    @Schema(description = "상영기간 시작일", example = "2021-10-10")
    val screeningStartDate: LocalDate?,
    @Schema(description = "상영기간 종료일", example = "2021-10-10")
    val screeningEndDate: LocalDate?,
    @Schema(description = "출품기간 시작일", example = "2021-10-10")
    val exhibitStartDate: LocalDate?,
    @Schema(description = "출품기간 종료일", example = "2021-10-10")
    val exhibitEndDate: LocalDate?,
    @Schema(description = "영화제 보여주는 시작일", example = "2021-10-10")
    val showStartDate: LocalDate?,
    @Schema(description = "주최", example = "방송통신 위원회")
    val agency: String,
    @Schema(description = "소개", example = "#참가자격...")
    val details: String,
    @Schema(description = "조회수", example = "5")
    val viewCount: Long,
    @Schema(description = "스크랩수", example = "5")
    val scrapCount: Long,
    @Schema(description = "스크랩 여부", example = "false")
    val isScrap: Boolean = false,
    @Schema(description = "보러가기 링크 RUL", example = "https://www.naver.com")
    val linkUrl: String,
) {

    @get:Schema(description = "상영기간 D-Day", example = "D-5")
    val screeningDDay: String
        get() = if (screeningStartDate == null || screeningEndDate == null) {
            "미정"
        } else {
            DateTimeFormat.calculateDays(screeningEndDate)
        }

    @get:Schema(description = "상영기간", example = "2023.1.16(월) ~ 2023.6.30(금)")
    val screeningDate: String
        get() = if (screeningStartDate == null || screeningEndDate == null) {
            "미정"
        } else {
            screeningStartDate.format(
                DateTimeFormatter.ofPattern(
                    "yyyy.M.d(E)",
                    Locale.KOREAN
                )
            ) + " ~ " + screeningEndDate.format(DateTimeFormatter.ofPattern("yyyy.M.d(E)", Locale.KOREAN))
        }

    @get:Schema(description = "출품기간", example = "2023.1.16(월) ~ 2023.6.30(금)")
    val exhibitDate: String
        get() = if (exhibitStartDate == null || exhibitEndDate == null) {
            "미정"
        } else {
            exhibitStartDate.format(
                DateTimeFormatter.ofPattern(
                    "yyyy.M.d(E)",
                    Locale.KOREAN
                )
            ) + " ~ " + exhibitEndDate.format(DateTimeFormatter.ofPattern("yyyy.M.d(E)", Locale.KOREAN))
        }

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
