package com.fone.jobOpening.presentation.dto

import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.common.entity.Type
import com.fone.jobOpening.domain.entity.JobOpening
import com.fone.jobOpening.domain.entity.JobOpeningScrap
import com.fone.jobOpening.infrastructure.toLocation
import com.fone.jobOpening.presentation.dto.common.JobOpeningDto
import com.fone.user.domain.enum.Job
import io.swagger.v3.oas.annotations.media.Schema

data class RegisterJobOpeningRequest(
    @Schema(description = "1번째 페이지")
    val firstPage: FirstPage,

    @Schema(description = "2번째 페이지")
    val secondPage: SecondPage,

    @Schema(description = "3번째 페이지")
    val thirdPage: ThirdPage,

    @Schema(description = "4번째 페이지")
    val fourthPage: FourthPage,

    @Schema(description = "5번째 페이지")
    val fifthPage: FifthPage,

    @Schema(description = "6번째 페이지")
    val sixthPage: SixthPage,

    @Schema(description = "7번째 페이지")
    val seventhPage: SeventhPage,

    @Schema(description = "모집유형", example = "ACTOR")
    val type: Type,
)

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
        jobOpening =
        JobOpeningDto(
            jobOpening,
            userJobOpeningScrapMap,
            domains,
            categories,
            nickname,
            profileUrl,
            job,
            jobOpening.imageUrls.map { it.url }.toList(),
            isVerified
        )
    )
}

suspend fun RegisterJobOpeningRequest.toEntity(userId: Long): JobOpening {
    return JobOpening(
        contactMethod = firstPage.contactMethod,
        contact = firstPage.contact,
        title = secondPage.title,
        recruitmentStartDate = secondPage.recruitmentStartDate,
        recruitmentEndDate = secondPage.recruitmentEndDate,
        representativeImageUrl = secondPage.representativeImageUrl,
        casting = thirdPage.casting,
        numberOfRecruits = thirdPage.numberOfRecruits,
        gender = thirdPage.gender,
        ageMax = thirdPage.ageMax ?: 200,
        ageMin = thirdPage.ageMin ?: 0,
        careers = thirdPage.careers.map { it.toString() },
        produce = fourthPage.produce,
        workTitle = fourthPage.workTitle,
        director = fourthPage.director,
        genres = fourthPage.genres.map { it.toString() },
        logline = fourthPage.logline,
        location = fifthPage.toLocation(),
        workingStartDate = fifthPage.workingStartDate,
        workingEndDate = fifthPage.workingEndDate,
        selectedDays = fifthPage.selectedDays.map { it.toString() },
        workingStartTime = fifthPage.workingStartTime,
        workingEndTime = fifthPage.workingEndTime,
        salaryType = fifthPage.salaryType,
        salary = fifthPage.salary,
        details = sixthPage.details,
        manager = seventhPage.manager,
        email = seventhPage.email,
        type = type,
        userId = userId
    )
}
