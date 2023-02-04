package com.fone.filmone.presentation.job_opening

import com.fone.filmone.domain.common.DomainType
import com.fone.filmone.domain.common.Gender
import com.fone.filmone.domain.common.Interest
import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.job_opening.entity.JobOpening
import com.fone.filmone.domain.job_opening.entity.JobOpeningScrap
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

class RetrieveJobOpeningDto {

    data class RetrieveJobOpeningsRequest(
        val type: Type,
        val gender: Gender,
        val ageMax: Int,
        val ageMin: Int,
        val interests: List<Interest>,
        val domains: List<DomainType>,
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