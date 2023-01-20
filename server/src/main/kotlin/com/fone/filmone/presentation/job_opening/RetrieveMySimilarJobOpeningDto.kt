package com.fone.filmone.presentation.job_opening

import com.fone.filmone.domain.job_opening.entity.JobOpening

class RetrieveMySimilarJobOpeningDto {

    data class RetrieveMySimilarJobOpeningResponse(
        val jobOpenings: List<JobOpeningDto>,
    ) {
        constructor(jobOpeningList: ArrayList<JobOpening>) : this(
            jobOpenings = jobOpeningList.map { JobOpeningDto(it) }.toList()
        )
    }
}