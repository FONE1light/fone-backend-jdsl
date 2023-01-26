package com.fone.filmone.presentation.job_opening

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
            pageable: Pageable,
        ) : this(
            jobOpenings = PageImpl(
                jobOpeningList.map { JobOpeningDto(it, userJobOpeningScrapMap) }.toList(),
                pageable,
                jobOpeningList.size.toLong(),
            )
        )
    }
}