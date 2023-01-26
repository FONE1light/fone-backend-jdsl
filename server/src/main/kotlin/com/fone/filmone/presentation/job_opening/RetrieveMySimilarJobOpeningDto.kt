package com.fone.filmone.presentation.job_opening

import com.fone.filmone.domain.job_opening.entity.JobOpening
import com.fone.filmone.domain.job_opening.entity.JobOpeningScrap

class RetrieveMySimilarJobOpeningDto {

    data class RetrieveMySimilarJobOpeningResponse(
        val jobOpenings: List<JobOpeningDto>,
    ) {
        constructor(
            jobOpeningList: List<JobOpening>,
            userJobOpeningScrapMap: Map<Long, JobOpeningScrap?>,
        ) : this(
            jobOpenings = jobOpeningList.map { JobOpeningDto(it, userJobOpeningScrapMap) }.toList()
        )
    }
}