package com.fone.jobOpening.presentation.dto

import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.jobOpening.domain.entity.JobOpening
import com.fone.jobOpening.domain.entity.JobOpeningScrap
import com.fone.jobOpening.presentation.dto.common.JobOpeningDto
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
            jobOpenings =
                PageImpl(
                    jobOpeningList
                        .map {
                            JobOpeningDto(
                                it,
                                userJobOpeningScrapMap,
                                jobOpeningDomains[it.id!!] ?: listOf(),
                                jobOpeningCategories[it.id!!] ?: listOf(),
                            )
                        }
                        .toList(),
                    pageable,
                    jobOpeningList.size.toLong(),
                )
        )
    }
}
