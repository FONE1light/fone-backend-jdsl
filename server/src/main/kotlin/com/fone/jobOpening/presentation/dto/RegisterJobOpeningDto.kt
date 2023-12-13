package com.fone.jobOpening.presentation.dto

import com.fone.common.entity.Career
import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.common.entity.Gender
import com.fone.common.entity.Type
import com.fone.jobOpening.domain.entity.JobOpening
import com.fone.jobOpening.domain.entity.JobOpeningScrap
import com.fone.jobOpening.presentation.dto.common.JobOpeningDto
import com.fone.jobOpening.presentation.dto.common.WorkDto
import com.fone.user.domain.enum.Job
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

class RegisterJobOpeningDto {

    data class RegisterJobOpeningRequest(
        @Schema(description = "모집제목", example = "많은 이들의 시선보다 ..") val title: String,
        @Schema(description = "작품의 성격", example = "[\"ACTOR\",\"ACTRESS\"]") val categories: List<CategoryType>,
        @Schema(
            description = "recruitmentEndDate 로 대체합니다.",
            example = "2021-10-10",
            deprecated = true
        )
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @Deprecated("recruitmentEndDate 로 대체합니다.")
        val deadline: LocalDate?,
        @Schema(description = "모집배역", example = "30대 중반 경찰") val casting: String?,
        @Schema(description = "모집인원", example = "1") val numberOfRecruits: Int,
        @Schema(description = "성별", example = "MAN") val gender: Gender,
        @Schema(description = "최대 나이", example = "40") val ageMax: Int,
        @Schema(description = "최소 나이", example = "20") val ageMin: Int,
        @Schema(description = "경력", example = "NEWCOMER") val career: Career,
        @Schema(description = "모집유형", example = "ACTOR") val type: Type,
        @Schema(description = "모집분야", example = "[\"PLANNING\",\"SCENARIO\"]") val domains: List<DomainType>?,
        @Schema(description = "작품정보") val work: WorkDto,

        @Schema(
            description = "모집 기간 시작일(null이면 상시모집)",
            example = "2021.10.10"
        )
        @DateTimeFormat(pattern = "yyyy.MM.dd")
        val recruitmentStartDate: LocalDate?,
        @Schema(
            description = "모집 기간 종료일(null이면 상시모집)",
            example = "2021.10.11"
        )
        @DateTimeFormat(pattern = "yyyy.MM.dd")
        val recruitmentEndDate: LocalDate?,
        @Schema(description = "이미지(대표 이미지)", example = "https://www.naver.com") val representativeImageUrl: String?,
        @Schema(
            description = "이미지",
            example = "[\"https://www.naver.com\",\"https://www.naver.com\"]"
        ) val imageUrls: List<String>,
    ) {
        fun toEntity(userId: Long): JobOpening {
            return JobOpening(
                title = title,
                deadline = deadline,
                casting = casting,
                numberOfRecruits = numberOfRecruits,
                gender = gender,
                ageMax = ageMax,
                ageMin = ageMin,
                career = career,
                type = type,
                userId = userId,
                viewCount = 0,
                scrapCount = 0,
                work = work.toEntity(),
                recruitmentStartDate = recruitmentStartDate,
                recruitmentEndDate = recruitmentEndDate,
                representativeImageUrl = representativeImageUrl
            )
        }
    }

    data class RegisterJobOpeningResponse(
        val jobOpening: JobOpeningDto,
    ) {

        constructor(
            jobOpening: JobOpening,
            userJobOpeningScrapMap: Map<Long, JobOpeningScrap?>,
            domains: List<DomainType>?,
            categories: List<CategoryType>,
            nickname: String,
            profileUrl: String,
            job: Job,
            isVerified: Boolean,
        ) : this(
            jobOpening = JobOpeningDto(
                jobOpening,
                userJobOpeningScrapMap,
                domains,
                categories,
                nickname,
                profileUrl,
                job,
                jobOpening.images.map { it.url }.toList(),
                isVerified
            )
        )
    }
}
