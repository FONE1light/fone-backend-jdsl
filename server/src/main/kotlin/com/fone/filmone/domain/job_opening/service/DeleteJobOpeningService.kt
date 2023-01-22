package com.fone.filmone.domain.job_opening.service

import com.fone.filmone.common.exception.InvalidJobOpeningUserIdException
import com.fone.filmone.common.exception.NotFoundJobOpeningException
import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.infrastructure.job_opening.JobOpeningRepository
import com.fone.filmone.infrastructure.job_opening.WorkRepository
import com.fone.filmone.infrastructure.user.UserRepository
import org.springframework.stereotype.Service

@Service
class DeleteJobOpeningService(
    private val jobOpeningRepository: JobOpeningRepository,
    private val workRepository: WorkRepository,
    private val userRepository: UserRepository,
) {

    suspend fun deleteJobOpening(email: String, jobOpeningId: Long) {
        val user = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        val jobOpening = jobOpeningRepository.findById(jobOpeningId)
            ?: throw NotFoundJobOpeningException()

        val work = workRepository.findByJobOpeningId(jobOpeningId)
            ?: throw NotFoundJobOpeningException()

        if (jobOpening.userId != user.id) {
            throw InvalidJobOpeningUserIdException()
        }

        jobOpening.delete()
        work.delete()

        jobOpeningRepository.save(jobOpening)
        workRepository.save(work)
    }
}