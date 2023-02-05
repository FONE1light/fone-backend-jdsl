package com.fone.filmone.domain.job_opening.service

import com.fone.common.exception.InvalidJobOpeningUserIdException
import com.fone.common.exception.NotFoundJobOpeningException
import com.fone.common.exception.NotFoundUserException
import com.fone.filmone.domain.job_opening.repository.JobOpeningCategoryRepository
import com.fone.filmone.domain.job_opening.repository.JobOpeningDomainRepository
import com.fone.filmone.domain.job_opening.repository.JobOpeningRepository
import com.fone.filmone.domain.user.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
class DeleteJobOpeningService(
    private val jobOpeningRepository: JobOpeningRepository,
    private val jobOpeningDomainRepository: JobOpeningDomainRepository,
    private val jobOpeningCategoryRepository: JobOpeningCategoryRepository,
    private val userRepository: UserRepository,
) {

    suspend fun deleteJobOpening(email: String, jobOpeningId: Long) {
        val user =
            userRepository.findByNicknameOrEmail(null, email) ?: throw NotFoundUserException()

        val jobOpening = jobOpeningRepository.findByTypeAndId(null, jobOpeningId)
            ?: throw NotFoundJobOpeningException()

        if (jobOpening.userId != user.id) {
            throw InvalidJobOpeningUserIdException()
        }

        jobOpening.delete()

        coroutineScope {
            val jobOpening = async {
                jobOpeningRepository.save(jobOpening)
            }
            val jobOpeningDomain = async {
                jobOpeningDomainRepository.deleteByJobOpeningId(jobOpeningId)
            }
            val jobOpeningCategory = async {
                jobOpeningCategoryRepository.deleteByJobOpeningId(jobOpeningId)
            }

            jobOpening.await()
            jobOpeningDomain.await()
            jobOpeningCategory.await()
        }
    }
}