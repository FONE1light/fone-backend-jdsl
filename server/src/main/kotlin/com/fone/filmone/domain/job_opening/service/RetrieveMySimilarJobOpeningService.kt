package com.fone.filmone.domain.job_opening.service

import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.infrastructure.job_opening.JobOpeningRepository
import com.fone.filmone.infrastructure.user.UserRepository
import com.fone.filmone.presentation.job_opening.RetrieveMySimilarJobOpeningDto.RetrieveMySimilarJobOpeningResponse
import org.springframework.stereotype.Service

@Service
class RetrieveMySimilarJobOpeningService(
    private val jobOpeningRepository: JobOpeningRepository,
    private val userRepository: UserRepository,
) {

    suspend fun retrieveMySimilarJobOpening(email: String): RetrieveMySimilarJobOpeningResponse {
        val user = userRepository.findByEmail(email) ?: throw NotFoundUserException()
        val jobType = if (user.job.toString() == "ACTOR") "ACTOR" else "STAFF"
        val jobOpenings = jobOpeningRepository.findTop5ByType(jobType)

        return RetrieveMySimilarJobOpeningResponse(jobOpenings)
    }
}