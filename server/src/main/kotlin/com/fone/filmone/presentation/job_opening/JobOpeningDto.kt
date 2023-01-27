package com.fone.filmone.presentation.job_opening

import com.fone.filmone.domain.common.*
import com.fone.filmone.domain.job_opening.entity.JobOpening
import com.fone.filmone.domain.job_opening.entity.JobOpeningScrap

data class JobOpeningDto(
    val id: Long,
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
    val viewCount: Long,
    val work: WorkDto,
    val isScrap: Boolean = false,
) {
    constructor(
        jobOpening: JobOpening,
        userJobOpeningScrapMap: Map<Long, JobOpeningScrap?>,
    ) : this(
        id = jobOpening.id!!,
        title = jobOpening.title,
        interests = jobOpening.interests.map { Interest(it) }.toList(),
        deadline = jobOpening.deadline,
        casting = jobOpening.casting,
        numberOfRecruits = jobOpening.numberOfRecruits,
        gender = jobOpening.gender,
        ageMax = jobOpening.ageMax,
        ageMin = jobOpening.ageMin,
        career = jobOpening.career,
        type = jobOpening.type,
        domains = jobOpening.domains.map { Domain(it) }.toList(),
        viewCount = jobOpening.viewCount,
        work = WorkDto(jobOpening.work),
        isScrap = userJobOpeningScrapMap.get(jobOpening.id!!) != null,
    )
}
