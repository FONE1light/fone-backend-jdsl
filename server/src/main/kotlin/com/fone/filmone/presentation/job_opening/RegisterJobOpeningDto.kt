package com.fone.filmone.presentation.job_opening

import com.fone.common.entity.*
import com.fone.filmone.domain.job_opening.entity.JobOpening
import com.fone.filmone.domain.job_opening.entity.JobOpeningScrap
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

class RegisterJobOpeningDto {

    data class RegisterJobOpeningRequest(
        val title: String,
        val categories: List<CategoryType>,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        val deadline: LocalDate,
        val casting: String,
        val numberOfRecruits: Int,
        val gender: Gender,
        val ageMax: Int,
        val ageMin: Int,
        val career: Career,
        val type: Type,
        val domains: List<DomainType>,
        val work: WorkDto,
    ) {
        fun toEntity(userId: Long): JobOpening {

            return JobOpening(
                title = title,
                deadline = deadline,
                casting = casting,
                numberOfRecruits = numberOfRecruits,
                gender = gender,
                ageMax = ageMax,
                ageMin = ageMin,
                career = career,
                type = type,
                userId = userId,
                viewCount = 0,
                scrapCount = 0,
                work = work.toEntity(),
            )
        }
    }

    data class RegisterJobOpeningResponse(
        val jobOpening: JobOpeningDto,
    ) {

        constructor(
            jobOpening: JobOpening,
            userJobOpeningScrapMap: Map<Long, JobOpeningScrap?>,
            domains: List<DomainType>,
            categories: List<CategoryType>,
        ) : this(
            jobOpening = JobOpeningDto(jobOpening, userJobOpeningScrapMap, domains, categories)
        )
    }
}