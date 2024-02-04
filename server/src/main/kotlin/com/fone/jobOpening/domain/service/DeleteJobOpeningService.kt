package com.fone.jobOpening.domain.service

import com.fone.common.exception.InvalidJobOpeningUserIdException
import com.fone.common.exception.NotFoundJobOpeningException
import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.jobOpening.domain.repository.JobOpeningCategoryRepository
import com.fone.jobOpening.domain.repository.JobOpeningDomainRepository
import com.fone.jobOpening.domain.repository.JobOpeningRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class DeleteJobOpeningService(
    private val jobOpeningRepository: JobOpeningRepository,
    private val jobOpeningDomainRepository: JobOpeningDomainRepository,
    private val jobOpeningCategoryRepository: JobOpeningCategoryRepository,
    private val userRepository: UserCommonRepository,
) {

    @Transactional
    suspend fun deleteJobOpening(email: String, jobOpeningId: Long) {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()
        val jobOpening = jobOpeningRepository.findByTypeAndId(null, jobOpeningId) ?: throw NotFoundJobOpeningException()

        if (jobOpening.userId != userId) {
            throw InvalidJobOpeningUserIdException()
        }
        jobOpening.delete()
        jobOpeningRepository.save(jobOpening)
        jobOpeningDomainRepository.deleteByJobOpeningId(jobOpeningId)
        jobOpeningCategoryRepository.deleteByJobOpeningId(jobOpeningId)
    }
}
