package com.fone.jobOpening.presentation.dto.common

import com.fone.common.entity.Career
import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.common.entity.Genre
import com.fone.common.entity.Type
import com.fone.common.entity.Weekday
import com.fone.common.utils.DateTimeFormat
import com.fone.jobOpening.domain.entity.JobOpening
import com.fone.jobOpening.domain.entity.JobOpeningScrap
import com.fone.jobOpening.presentation.dto.FifthPage
import com.fone.jobOpening.presentation.dto.FirstPage
import com.fone.jobOpening.presentation.dto.FourthPage
import com.fone.jobOpening.presentation.dto.SecondPage
import com.fone.jobOpening.presentation.dto.SeventhPage
import com.fone.jobOpening.presentation.dto.SixthPage
import com.fone.jobOpening.presentation.dto.ThirdPage
import com.fone.user.domain.enum.Job
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class JobOpeningDto(
    @Schema(description = "job opening index", example = "1") val id: Long,

    @Schema(description = "1번째 페이지") val firstPage: FirstPage,

    @Schema(description = "2번째 페이지") val secondPage: SecondPage,

    @Schema(description = "3번째 페이지") val thirdPage: ThirdPage,

    @Schema(description = "4번째 페이지") val fourthPage: FourthPage,

    @Schema(description = "5번째 페이지") val fifthPage: FifthPage,

    @Schema(description = "6번째 페이지") val sixthPage: SixthPage,

    @Schema(description = "7번째 페이지") val seventhPage: SeventhPage,

    @Schema(description = "모집유형", example = "ACTOR") val type: Type,
    @Schema(description = "조회수", example = "1") val viewCount: Long,
    @Schema(description = "스크랩수", example = "1") val scrapCount: Long,
    @Schema(description = "스크랩 여부", example = "false") val isScrap: Boolean = false,
    @Schema(description = "닉네임", example = "김매니저") val userNickname: String,
    @Schema(description = "프로필 이미지", example = "https://www.naver.com") val userProfileUrl: String,
    @Schema(description = "작성일", example = "2021-10-10") val createdAt: LocalDateTime,
    @Schema(description = "유저 직업", example = "ACTOR") val userJob: Job,
    @Schema(description = "공식 인증 여부", example = "false") val isVerified: Boolean,
) {
    @get:Schema(description = "D-day", example = "D-1")
    val dDay: String
        get() = DateTimeFormat.calculateDays(secondPage.recruitmentEndDate)

    @get:Schema(description = "근무기간", example = "2023.01.16(월) ~ 2023.06.30(금)")
    val workingDate: String
        get() = if (fifthPage.workingStartDate == null || fifthPage.workingEndDate == null) {
            "상시모집"
        } else {
            fifthPage.workingStartDate.format(
                DateTimeFormatter.ofPattern(
                    "yyyy. MM. dd(E)",
                    Locale.KOREAN
                )
            ) + " ~ " + fifthPage.workingEndDate.format(DateTimeFormatter.ofPattern("yyyy. MM. dd(E)", Locale.KOREAN))
        }

    @get:Schema(description = "연락하기 활성화 여부", example = "true")
    val isContactable: Boolean
        get() = isContactable(secondPage.recruitmentStartDate, secondPage.recruitmentEndDate)

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
        id = jobOpening.id ?: 0L,
        firstPage = FirstPage(
            contactMethod = jobOpening.contactMethod,
            contact = jobOpening.contact
        ),
        secondPage = SecondPage(
            title = jobOpening.title,
            categories = categories,
            recruitmentStartDate = jobOpening.recruitmentStartDate,
            recruitmentEndDate = jobOpening.recruitmentEndDate,
            imageUrls = imageUrls,
            representativeImageUrl = jobOpening.representativeImageUrl
        ),
        thirdPage = ThirdPage(
            casting = jobOpening.casting,
            domains = domains,
            numberOfRecruits = jobOpening.numberOfRecruits,
            gender = jobOpening.gender,
            ageMax = jobOpening.ageMax,
            ageMin = jobOpening.ageMin,
            careers = jobOpening.careers.map { Career.valueOf(it) }
        ),
        fourthPage = FourthPage(
            produce = jobOpening.produce,
            workTitle = jobOpening.workTitle,
            director = jobOpening.director,
            genres = jobOpening.genres.map { Genre.valueOf(it) }.toSet(),
            logline = jobOpening.logline
        ),
        fifthPage = FifthPage(
            workingCity = jobOpening.location.region,
            workingDistrict = jobOpening.location.district,
            workingStartDate = jobOpening.workingStartDate,
            workingEndDate = jobOpening.workingEndDate,
            selectedDays = jobOpening.selectedDays.map { Weekday.valueOf(it) }.toSet(),
            workingStartTime = jobOpening.workingStartTime,
            workingEndTime = jobOpening.workingEndTime,
            salaryType = jobOpening.salaryType,
            salary = jobOpening.salary
        ),
        sixthPage = SixthPage(
            details = jobOpening.details
        ),
        seventhPage = SeventhPage(
            manager = jobOpening.manager,
            email = jobOpening.email
        ),
        type = jobOpening.type,
        viewCount = jobOpening.viewCount,
        scrapCount = jobOpening.scrapCount,
        isScrap = userJobOpeningScrapMap[jobOpening.id!!] != null,
        userNickname = nickname,
        userProfileUrl = profileUrl,
        createdAt = jobOpening.createdAt,
        userJob = job,
        isVerified = isVerified
    )
}

fun isContactable(startDate: LocalDate?, endDate: LocalDate?): Boolean {
    return if (startDate == null || endDate == null) {
        true
    } else {
        !(
            startDate.isAfter(LocalDate.now()) ||
                endDate.isBefore(LocalDate.now())
            )
    }
}
