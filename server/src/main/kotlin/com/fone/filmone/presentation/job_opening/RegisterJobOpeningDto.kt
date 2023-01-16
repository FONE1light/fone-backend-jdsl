package com.fone.filmone.presentation.job_opening

import com.fone.filmone.domain.common.Career
import com.fone.filmone.domain.common.Gender
import com.fone.filmone.domain.common.Interest
import com.fone.filmone.domain.job_opening.entity.JobOpening
import com.fone.filmone.domain.job_opening.enum.Domain
import com.fone.filmone.domain.job_opening.enum.Type
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
        val work: Work,
    ) {
        fun toEntity(userId: Long): JobOpening {

            return JobOpening(
                title = title,
                interests = interests.joinToString(","),
                deadline = deadline.toString(),
                casting = casting,
                numberOfRecruits = numberOfRecruits,
                gender = gender,
                ageMax = ageMax,
                ageMin = ageMin,
                career = career,
                type = type,
                domains = domains.joinToString(","),
                userId = userId,
            )
        }
    }

    data class Work(
        val produce: String,
        val title: String,
        val director: String,
        val genre: String,
        val logline: String,
        val location: String,
        val period: String,
        val pay: String,
        val details: String,
        val manager: String,
        val email: String,
    ) {
        fun toEntity(jobOpeningId: Long): com.fone.filmone.domain.job_opening.entity.Work {

            return com.fone.filmone.domain.job_opening.entity.Work(
                produce = produce,
                title = title,
                director = director,
                genre = genre,
                logline = logline,
                location = location,
                period = period,
                pay = pay,
                details = details,
                manager = manager,
                email = email,
                jobOpeningId = jobOpeningId,
            )
        }
    }

    data class RegisterJobOpeningResponse(
        val title: String,
    ) {

        constructor(
            jobOpening: JobOpening
        ) : this(
            title = jobOpening.title
        )
    }
}