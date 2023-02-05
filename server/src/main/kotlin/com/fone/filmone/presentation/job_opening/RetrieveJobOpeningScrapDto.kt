package com.fone.filmone.presentation.job_opening

import com.fone.filmone.domain.common.CategoryType
import com.fone.filmone.domain.common.DomainType
import com.fone.filmone.domain.job_opening.entity.JobOpening
import com.fone.filmone.domain.job_opening.entity.JobOpeningScrap
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

class RetrieveJobOpeningScrapDto {

    data class RetrieveJobOpeningScrapResponse(
        val jobOpenings: Slice<JobOpeningDto>,
    ) {

        constructor(
            jobOpeningList: List<JobOpening>,
            userJobOpeningScrapMap: Map<Long, JobOpeningScrap?>,
            jobOpeningDomains: Map<Long, List<DomainType>>,
            jobOpeningCategories: Map<Long, List<CategoryType>>,
            pageable: Pageable,
        ) : this(
            jobOpenings = PageImpl(
                jobOpeningList.map {
                    JobOpeningDto(
                        it,
                        userJobOpeningScrapMap,
                        jobOpeningDomains[it.id!!] ?: listOf(),
                        jobOpeningCategories[it.id!!] ?: listOf(),
                    )
                }.toList(),
                pageable,
                jobOpeningList.size.toLong(),
            )
        )
    }
}