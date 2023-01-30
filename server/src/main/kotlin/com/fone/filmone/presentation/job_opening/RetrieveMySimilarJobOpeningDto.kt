package com.fone.filmone.presentation.job_opening

import com.fone.filmone.domain.job_opening.entity.JobOpening
import com.fone.filmone.domain.job_opening.entity.JobOpeningScrap
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

class RetrieveMySimilarJobOpeningDto {

    data class RetrieveMySimilarJobOpeningResponse(
        val jobOpenings: Slice<JobOpeningDto>,
    ) {
        constructor(
            jobOpenings: Slice<JobOpening>,
            userJobOpeningScrapMap: Map<Long, JobOpeningScrap?>,
            pageable: Pageable
        ) : this(
//            jobOpenings = jobOpenings.map { JobOpeningDto(it, userJobOpeningScrapMap) }.toList()
            jobOpenings = PageImpl(
                jobOpenings.map { JobOpeningDto(it, userJobOpeningScrapMap) }.toList(),
                pageable,
                jobOpenings.size.toLong()
            )
        )
    }
}