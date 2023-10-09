package com.fone.jobOpening.presentation.dto

import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.common.entity.Gender
import com.fone.common.entity.Type
import com.fone.jobOpening.domain.entity.JobOpening
import com.fone.jobOpening.domain.entity.JobOpeningScrap
import com.fone.jobOpening.presentation.dto.common.JobOpeningDto
import com.fone.user.domain.entity.User
import com.fone.user.domain.enum.Job
import io.swagger.annotations.ApiModelProperty
import org.springframework.data.domain.Page

class RetrieveJobOpeningDto {

    data class RetrieveJobOpeningsRequest(
        @ApiModelProperty(value = "타입", example = "ACTOR", required = true) val type: Type,
        @ApiModelProperty(value = "성별", required = false) val genders: List<Gender> = emptyList(),
        @ApiModelProperty(value = "최대 나이", required = false, example = "200") val ageMax: Int = 200,
        @ApiModelProperty(value = "최소 나이", required = false, example = "0") val ageMin: Int = 0,
        @ApiModelProperty(
            value = "카테고리",
            required = false
        ) val categories: List<CategoryType> = emptyList(),
        @ApiModelProperty(value = "분야", required = false) val domains: List<DomainType> = emptyList(),
    )

    data class RetrieveJobOpeningsResponse(
        val jobOpenings: Page<JobOpeningDto>,
    ) {

        constructor(
            jobOpeningPage: Page<JobOpening>,
            userJobOpeningScrapMap: Map<Long, JobOpeningScrap?>,
            jobOpeningDomains: Map<Long, List<DomainType>>,
            jobOpeningCategories: Map<Long, List<CategoryType>>,
            jobOpeningUsers: Map<Long?, User>,
        ) : this(
            jobOpenings = jobOpeningPage.map {
                JobOpeningDto(
                    it,
                    userJobOpeningScrapMap,
                    jobOpeningDomains[it.id!!] ?: listOf(),
                    jobOpeningCategories[it.id!!] ?: listOf(),
                    jobOpeningUsers[it.userId]?.nickname ?: "",
                    jobOpeningUsers[it.userId]?.profileUrl ?: "",
                    jobOpeningUsers[it.userId]?.job ?: Job.ACTOR
                )
            }
        )
    }

    data class RetrieveJobOpeningResponse(
        val jobOpening: JobOpeningDto,
    ) {

        constructor(
            reqJobOpening: JobOpening,
            userJobOpeningScrapMap: Map<Long, JobOpeningScrap?>,
            domains: List<DomainType>,
            categories: List<CategoryType>,
            nickname: String,
            profileUrl: String,
            job: Job,
        ) : this(
            jobOpening = JobOpeningDto(
                reqJobOpening,
                userJobOpeningScrapMap,
                domains,
                categories,
                nickname,
                profileUrl,
                job
            )
        )
    }
}
