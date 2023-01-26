package com.fone.filmone.presentation.job_opening

import com.fone.filmone.domain.common.*
import com.fone.filmone.domain.job_opening.entity.JobOpening
import org.springframework.format.annotation.DateTimeFormat
import java.sql.Date

class RegisterJobOpeningDto {

    data class RegisterJobOpeningRequest(
        val title: String,
        val interests: List<Interest>,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        val deadline: Date,
        val casting: String,
        val numberOfRecruits: Int,
        val gender: Gender,
        val ageMax: Int,
        val ageMin: Int,
        val career: Career,
        val type: Type,
        val domains: List<Domain>,
        val work: WorkDto,
    ) {
        fun toEntity(userId: Long): JobOpening {

            return JobOpening(
                title = title,
                interests = interests,
                deadline = deadline.toString(),
                casting = casting,
                numberOfRecruits = numberOfRecruits,
                gender = gender,
                ageMax = ageMax,
                ageMin = ageMin,
                career = career,
                type = type,
                domains = domains,
                userId = userId,
                viewCount = 0,
                work = work.toEntity()
            )
        }
    }

    data class RegisterJobOpeningResponse(
        val jobOpening: JobOpeningDto,
    ) {

        constructor(
            jobOpening: JobOpening,
        ) : this(
            jobOpening = JobOpeningDto(jobOpening)
        )
    }
}