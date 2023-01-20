package com.fone.filmone.presentation.job_opening

import com.fone.filmone.domain.common.*
import com.fone.filmone.domain.job_opening.entity.JobOpening

class RetrieveMySimilarJobOpeningDto {

    data class RetrieveMySimilarJobOpeningResponse(
        val jobOpenings: List<JobOpeningDto>,
    ) {
        constructor(jobOpeningList: ArrayList<JobOpening>) : this(
            jobOpenings = jobOpeningList.map { JobOpeningDto(it) }.toList()
        )
    }

    data class JobOpeningDto(
        val title: String,
        val interests: List<Interest>,
        val deadline: String,
        val casting: String,
        val numberOfRecruits: Int,
        val gender: Gender,
        val ageMax: Int,
        val ageMin: Int,
        val career: Career,
        val type: Type,
        val domains: List<Domain>,
    ) {
        constructor(
            jobOpening: JobOpening,
        ) : this(
            title = jobOpening.title,
            interests = jobOpening.interests.split(",").map { Interest(it) }.toList(),
            deadline = jobOpening.deadline,
            casting = jobOpening.casting,
            numberOfRecruits = jobOpening.numberOfRecruits,
            gender = jobOpening.gender,
            ageMax = jobOpening.ageMax,
            ageMin = jobOpening.ageMin,
            career = jobOpening.career,
            type = jobOpening.type,
            domains = jobOpening.domains.split(",").map { Domain(it) }.toList()
        )
    }
}