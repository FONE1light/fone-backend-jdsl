package com.fone.jobOpening.presentation.dto.common

import com.fone.common.entity.Career
import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.common.entity.Gender
import com.fone.common.entity.Type
import com.fone.common.utils.DateTimeFormat
import com.fone.jobOpening.domain.entity.JobOpening
import com.fone.jobOpening.domain.entity.JobOpeningScrap
import java.time.LocalDate
import java.time.LocalDateTime

data class JobOpeningDto(
    val id: Long,
    val title: String,
    val categories: List<CategoryType>,
    val deadline: LocalDate?,
    val casting: String?,
    val numberOfRecruits: Int,
    val gender: Gender,
    val ageMax: Int,
    val ageMin: Int,
    val careers: List<Career>,
    val type: Type,
    val domains: List<DomainType>?,
    val viewCount: Long,
    val scrapCount: Long,
    val work: WorkDto,
    val isScrap: Boolean = false,
    val nickname: String,
    val profileUrl: String,
    val createdAt: LocalDateTime,
) {
    val dDay: String
        get() = DateTimeFormat.calculateDays(deadline)

    constructor(
        jobOpening: JobOpening,
        userJobOpeningScrapMap: Map<Long, JobOpeningScrap?>,
        domains: List<DomainType>?,
        categories: List<CategoryType>,
        nickname: String,
        profileUrl: String,
    ) : this(
        id = jobOpening.id!!,
        title = jobOpening.title,
        categories = categories,
        deadline = jobOpening.deadline,
        casting = jobOpening.casting,
        numberOfRecruits = jobOpening.numberOfRecruits,
        gender = jobOpening.gender,
        ageMax = jobOpening.ageMax,
        ageMin = jobOpening.ageMin,
        careers = jobOpening.careers.toList(),
        type = jobOpening.type,
        domains = domains,
        viewCount = jobOpening.viewCount,
        scrapCount = jobOpening.scrapCount,
        work = WorkDto(jobOpening.work),
        isScrap = userJobOpeningScrapMap.get(jobOpening.id!!) != null,
        nickname = nickname,
        profileUrl = profileUrl,
        createdAt = jobOpening.createdAt
    )
}
