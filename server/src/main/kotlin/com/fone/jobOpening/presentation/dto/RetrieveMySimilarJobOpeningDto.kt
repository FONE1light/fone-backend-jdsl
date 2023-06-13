package com.fone.jobOpening.presentation.dto

import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.jobOpening.domain.entity.JobOpening
import com.fone.jobOpening.domain.entity.JobOpeningScrap
import com.fone.jobOpening.presentation.dto.common.JobOpeningDto
import org.springframework.data.domain.Page

class RetrieveMySimilarJobOpeningDto {

    data class RetrieveMySimilarJobOpeningResponse(
        val jobOpenings: Page<JobOpeningDto>,
    ) {
        constructor(
            jobOpenings: Page<JobOpening>,
            userJobOpeningScrapMap: Map<Long, JobOpeningScrap?>,
            jobOpeningDomains: Map<Long, List<DomainType>>,
            jobOpeningCategories: Map<Long, List<CategoryType>>,
        ) : this(
            jobOpenings = jobOpenings.map {
                JobOpeningDto(
                    it,
                    userJobOpeningScrapMap,
                    jobOpeningDomains[it.id!!] ?: listOf(),
                    jobOpeningCategories[it.id!!] ?: listOf()
                )
            }
        )
    }
}
