package com.fone.filmone.presentation.job_opening

import com.fone.common.utils.DateTimeFormat
import com.fone.filmone.domain.common.*
import com.fone.filmone.domain.job_opening.entity.JobOpening
import com.fone.filmone.domain.job_opening.entity.JobOpeningScrap
import java.time.LocalDate
import java.time.Period

data class JobOpeningDto(
    val id: Long,
    val title: String,
    val interests: List<Interest>,
    val deadline: LocalDate?,
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
    val dDay: String,
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
        dDay = DateTimeFormat.calculate(jobOpening.deadline)
    )
}
