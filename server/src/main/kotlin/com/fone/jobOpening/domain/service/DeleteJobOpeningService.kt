package com.fone.jobOpening.domain.service

import com.fone.common.exception.InvalidJobOpeningUserIdException
import com.fone.common.exception.NotFoundJobOpeningException
import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
class DeleteJobOpeningService(
    private val jobOpeningRepository: com.fone.jobOpening.domain.repository.JobOpeningRepository,
    private val jobOpeningDomainRepository: com.fone.jobOpening.domain.repository.JobOpeningDomainRepository,
    private val jobOpeningCategoryRepository: com.fone.jobOpening.domain.repository.JobOpeningCategoryRepository,
    private val userRepository: UserCommonRepository,
) {

    suspend fun deleteJobOpening(email: String, jobOpeningId: Long) {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        val jobOpening = jobOpeningRepository.findByTypeAndId(null, jobOpeningId) ?: throw NotFoundJobOpeningException()

        if (jobOpening.userId != userId) {
            throw InvalidJobOpeningUserIdException()
        }

        jobOpening.delete()

        coroutineScope {
            val jobOpening = async { jobOpeningRepository.save(jobOpening) }
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
