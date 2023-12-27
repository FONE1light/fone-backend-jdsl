package com.fone.jobOpening.presentation.dto.common

import com.fone.common.entity.Career
import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.common.entity.Gender
import com.fone.common.entity.Type
import com.fone.common.utils.DateTimeFormat
import com.fone.jobOpening.domain.entity.JobOpening
import com.fone.jobOpening.domain.entity.JobOpeningScrap
import com.fone.user.domain.enum.Job
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.time.LocalDateTime

data class JobOpeningDto(
    @Schema(description = "job opening index", example = "1")
    val id: Long,
    @Schema(description = "모집제목", example = "많은 이들의 시선보다 ..")
    val title: String,
    @Schema(description = "작품의 성격", example = "[\"ACTOR\",\"ACTRESS\"]")
    val categories: List<CategoryType>,
    @Schema(description = "모집배역", example = "30대 중반 경찰")
    val casting: String?,
    @Schema(description = "모집인원", example = "1")
    val numberOfRecruits: Int,
    @Schema(description = "성별", example = "MAN")
    val gender: Gender,
    @Schema(description = "최대 나이", example = "40")
    val ageMax: Int,
    @Schema(description = "최소 나이", example = "20")
    val ageMin: Int,
    @Schema(description = "경력", example = "NEWCOMER")
    val career: Career,
    @Schema(description = "모집유형", example = "ACTOR")
    val type: Type,
    @Schema(description = "모집분야", example = "[\"PLANNING\",\"SCENARIO\"]")
    val domains: List<DomainType>?,
    @Schema(description = "조회수", example = "1")
    val viewCount: Long,
    @Schema(description = "스크랩수", example = "1")
    val scrapCount: Long,
    @Schema(description = "작품정보")
    val work: WorkDto,
    @Schema(description = "스크랩 여부", example = "false")
    val isScrap: Boolean = false,
    @Schema(description = "닉네임", example = "김매니저")
    val nickname: String,
    @Schema(description = "프로필 이미지", example = "https://www.naver.com")
    val profileUrl: String,
    @Schema(description = "작성일", example = "2021-10-10")
    val createdAt: LocalDateTime,
    @Schema(description = "유저 직업", example = "ACTOR")
    val userJob: Job,
    @Schema(
        description = "모집 기간 시작일(null이면 상시모집)",
        example = "2021.10.10"
    )
    val recruitmentStartDate: LocalDate?,
    @Schema(
        description = "모집 기간 종료일(null이면 상시모집)",
        example = "2021.10.11"
    )
    val recruitmentEndDate: LocalDate?,
    @Schema(description = "이미지(대표 이미지)", example = "https://www.naver.com")
    val representativeImageUrl: String?,
    @Schema(
        description = "이미지",
        example = "[\"https://www.naver.com\",\"https://www.naver.com\"]"
    )
    val imageUrls: List<String>,
    @Schema(description = "공식 인증 여부", example = "false")
    val isVerified: Boolean,
) {
    @get:Schema(description = "D-day", example = "D-1")
    val dDay: String
        get() = DateTimeFormat.calculateDays(recruitmentEndDate)

    constructor(
        jobOpening: JobOpening,
        userJobOpeningScrapMap: Map<Long, JobOpeningScrap?>,
        domains: List<DomainType>?,
        categories: List<CategoryType>,
        nickname: String,
        profileUrl: String,
        job: Job,
        imageUrls: List<String>,
        isVerified: Boolean,
    ) : this(
        id = jobOpening.id!!,
        title = jobOpening.title,
        categories = categories,
        casting = jobOpening.casting,
        numberOfRecruits = jobOpening.numberOfRecruits,
        gender = jobOpening.gender,
        ageMax = jobOpening.ageMax,
        ageMin = jobOpening.ageMin,
        career = jobOpening.career,
        type = jobOpening.type,
        domains = domains,
        viewCount = jobOpening.viewCount,
        scrapCount = jobOpening.scrapCount,
        work = WorkDto(jobOpening.work, jobOpening.location?.region, jobOpening.location?.district),
        isScrap = userJobOpeningScrapMap.get(jobOpening.id!!) != null,
        nickname = nickname,
        profileUrl = profileUrl,
        createdAt = jobOpening.createdAt,
        userJob = job,
        recruitmentStartDate = jobOpening.recruitmentStartDate,
        recruitmentEndDate = jobOpening.recruitmentEndDate,
        representativeImageUrl = jobOpening.representativeImageUrl,
        imageUrls = imageUrls,
        isVerified = isVerified
    )
}
