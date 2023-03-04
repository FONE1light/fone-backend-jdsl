package com.fone.jobOpening.presentation.dto

import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.common.entity.Gender
import com.fone.common.entity.Type
import com.fone.jobOpening.domain.entity.JobOpening
import com.fone.jobOpening.domain.entity.JobOpeningScrap
import com.fone.jobOpening.presentation.dto.common.JobOpeningDto
import io.swagger.annotations.ApiModelProperty
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

class RetrieveJobOpeningDto {

    data class RetrieveJobOpeningsRequest(
        @ApiModelProperty(value = "타입", example = "ACTOR", required = true) val type: Type,
        @ApiModelProperty(value = "성별", required = false)
        val genders: List<Gender> = Gender.getAllEnum(),
        @ApiModelProperty(value = "최대 나이", required = false, example = "200") val ageMax: Int = 200,
        @ApiModelProperty(value = "최소 나이", required = false, example = "0") val ageMin: Int = 0,
        @ApiModelProperty(value = "카테고리", required = false)
        val categories: List<CategoryType> = CategoryType.getAllEnum(),
        @ApiModelProperty(value = "분야", required = false)
        val domains: List<DomainType> = DomainType.getAllEnum()
    )

    data class RetrieveJobOpeningsResponse(
        val jobOpenings: Slice<JobOpeningDto>
    ) {

        constructor(
            jobOpeningList: List<JobOpening>,
            userJobOpeningScrapMap: Map<Long, JobOpeningScrap?>,
            jobOpeningDomains: Map<Long, List<DomainType>>,
            jobOpeningCategories: Map<Long, List<CategoryType>>,
            pageable: Pageable
        ) : this(
            jobOpenings =
            PageImpl(
                jobOpeningList
                    .map {
                        JobOpeningDto(
                            it,
                            userJobOpeningScrapMap,
                            jobOpeningDomains[it.id!!] ?: listOf(),
                            jobOpeningCategories[it.id!!] ?: listOf()
                        )
                    }
                    .toList(),
                pageable,
                jobOpeningList.size.toLong()
            )
        )
    }

    data class RetrieveJobOpeningResponse(
        val jobOpening: JobOpeningDto
    ) {

        constructor(
            reqJobOpening: JobOpening,
            userJobOpeningScrapMap: Map<Long, JobOpeningScrap?>,
            domains: List<DomainType>,
            categories: List<CategoryType>
        ) : this(
            jobOpening = JobOpeningDto(reqJobOpening, userJobOpeningScrapMap, domains, categories)
        )
    }
}
