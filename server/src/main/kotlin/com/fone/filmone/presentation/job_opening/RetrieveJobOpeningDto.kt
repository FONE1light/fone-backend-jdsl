package com.fone.filmone.presentation.job_opening

import com.fone.filmone.domain.common.DomainType
import com.fone.filmone.domain.common.Gender
import com.fone.filmone.domain.common.Interest
import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.job_opening.entity.JobOpening
import com.fone.filmone.domain.job_opening.entity.JobOpeningScrap
import io.swagger.annotations.ApiModelProperty
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

class RetrieveJobOpeningDto {

    data class RetrieveJobOpeningsRequest(
        @ApiModelProperty(value = "타입", example = "ACTOR", required = true)
        val type: Type,
        @ApiModelProperty(value = "성별", required = false)
        val genders: List<Gender> = Gender.getAllEnum(),
        @ApiModelProperty(value = "최대 나이", required = false)
        val ageMax: Int = 200,
        @ApiModelProperty(value = "최소 나이", required = false)
        val ageMin: Int = 0,
        @ApiModelProperty(value = "카테고리", required = false)
        val interests: List<Interest> = Interest.getAllEnum(),
        @ApiModelProperty(value = "분야", required = false)
        val domains: List<DomainType> = DomainType.getAllEnum(),
    )

    data class RetrieveJobOpeningsResponse(
        val jobOpenings: Slice<JobOpeningDto>,
    ) {

        constructor(
            jobOpeningList: List<JobOpening>,
            userJobOpeningScrapMap: Map<Long, JobOpeningScrap?>,
            jobOpeningDomains: Map<Long, List<DomainType>>,
            pageable: Pageable,
        ) : this(
            jobOpenings = PageImpl(
                jobOpeningList.map {
                    JobOpeningDto(
                        it,
                        userJobOpeningScrapMap,
                        jobOpeningDomains[it.id!!] ?: listOf(),
                    )
                }.toList(),
                pageable,
                jobOpeningList.size.toLong()
            )
        )
    }

    data class RetrieveJobOpeningResponse(
        val jobOpening: JobOpeningDto,
    ) {

        constructor(
            reqJobOpening: JobOpening,
            userJobOpeningScrapMap: Map<Long, JobOpeningScrap?>,
            domains: List<DomainType>,
        ) : this(
            jobOpening = JobOpeningDto(reqJobOpening, userJobOpeningScrapMap, domains)
        )
    }
}