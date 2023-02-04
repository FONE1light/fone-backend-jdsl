package com.fone.filmone.presentation.job_opening

import com.fone.filmone.domain.common.DomainType
import com.fone.filmone.domain.job_opening.entity.JobOpening
import com.fone.filmone.domain.job_opening.entity.JobOpeningScrap
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

class RetrieveJobOpeningMyRegistrationDto {

    data class RetrieveJobOpeningMyRegistrationResponse(
        val jobOpenings: Slice<JobOpeningDto>,
    ) {

        constructor(
            jobOpenings: List<JobOpening>,
            userJobOpeningScrapMap: Map<Long, JobOpeningScrap?>,
            jobOpeningDomains: Map<Long, List<DomainType>>,
            pageable: Pageable,
        ) : this(
            jobOpenings = PageImpl(
                jobOpenings.map {
                    JobOpeningDto(
                        it,
                        userJobOpeningScrapMap,
                        jobOpeningDomains[it.id!!] ?: listOf(),
                    )
                }.toList(),
                pageable,
                jobOpenings.size.toLong(),
            )
        )
    }
}