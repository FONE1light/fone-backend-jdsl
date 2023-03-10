package com.fone.jobOpening.presentation.dto

import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.jobOpening.presentation.dto.common.JobOpeningDto
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

class RetrieveMySimilarJobOpeningDto {

    data class RetrieveMySimilarJobOpeningResponse(
        val jobOpenings: Slice<JobOpeningDto>,
    ) {
        constructor(
            jobOpenings: List<com.fone.jobOpening.domain.entity.JobOpening>,
            userJobOpeningScrapMap: Map<Long, com.fone.jobOpening.domain.entity.JobOpeningScrap?>,
            jobOpeningDomains: Map<Long, List<DomainType>>,
            jobOpeningCategories: Map<Long, List<CategoryType>>,
            pageable: Pageable,
        ) : this(
            jobOpenings = PageImpl(
                jobOpenings.map {
                    JobOpeningDto(
                        it,
                        userJobOpeningScrapMap,
                        jobOpeningDomains[it.id!!] ?: listOf(),
                        jobOpeningCategories[it.id!!] ?: listOf()
                    )
                }.toList(),
                pageable,
                jobOpenings.size.toLong()
            )
        )
    }
}
