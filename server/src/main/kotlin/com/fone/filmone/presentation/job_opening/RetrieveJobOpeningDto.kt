package com.fone.filmone.presentation.job_opening

import com.fone.filmone.domain.job_opening.entity.JobOpening
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

class RetrieveJobOpeningDto {

    data class RetrieveJobOpeningsResponse(
        val jobOpenings: Slice<JobOpeningDto>,
    ) {

        constructor(jobOpeningList: List<JobOpening>, pageable: Pageable) : this(
            jobOpenings = PageImpl(
                jobOpeningList.map { JobOpeningDto(it) }.toList(),
                pageable,
                jobOpeningList.size.toLong()
            )
        )
    }
}