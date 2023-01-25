package com.fone.filmone.domain.job_opening.service

import com.fone.filmone.common.exception.InvalidJobOpeningUserIdException
import com.fone.filmone.common.exception.NotFoundJobOpeningException
import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.domain.job_opening.repository.JobOpeningRepository
import com.fone.filmone.domain.job_opening.repository.WorkRepository
import com.fone.filmone.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class DeleteJobOpeningService(
    private val jobOpeningRepository: JobOpeningRepository,
    private val workRepository: WorkRepository,
    private val userRepository: UserRepository,
) {

    suspend fun deleteJobOpening(email: String, jobOpeningId: Long) {
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()

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