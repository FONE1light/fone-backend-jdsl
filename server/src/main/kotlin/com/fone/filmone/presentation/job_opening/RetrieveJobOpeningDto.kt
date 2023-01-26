package com.fone.filmone.presentation.job_opening

import com.fone.filmone.domain.job_opening.entity.JobOpening
import com.fone.filmone.domain.job_opening.entity.JobOpeningScrap
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

class RetrieveJobOpeningDto {

    data class RetrieveJobOpeningsResponse(
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
        ) : this(
            jobOpening = JobOpeningDto(reqJobOpening, userJobOpeningScrapMap)
        )
    }
}