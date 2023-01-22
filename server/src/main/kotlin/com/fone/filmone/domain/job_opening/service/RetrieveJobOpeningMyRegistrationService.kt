package com.fone.filmone.domain.job_opening.service

import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.infrastructure.job_opening.JobOpeningRepository
import com.fone.filmone.infrastructure.user.UserRepository
import com.fone.filmone.presentation.job_opening.RetrieveJobOpeningMyRegistrationDto.RetrieveJobOpeningMyRegistrationResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveJobOpeningMyRegistrationService(
    private val userRepository: UserRepository,
    private val jobOpeningRepository: JobOpeningRepository,
) {


    @Transactional(readOnly = true)
    suspend fun retrieveJobOpeningMyRegistrations(email: String):
            RetrieveJobOpeningMyRegistrationResponse {
        val user = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        val jobOpenings = jobOpeningRepository.findByUserId(user.id!!)

        return RetrieveJobOpeningMyRegistrationResponse(jobOpenings)
    }
}