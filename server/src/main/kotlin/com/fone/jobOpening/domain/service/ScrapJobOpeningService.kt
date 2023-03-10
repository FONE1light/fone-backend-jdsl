package com.fone.jobOpening.domain.service

import com.fone.common.exception.NotFoundJobOpeningException
import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ScrapJobOpeningService(
    private val userRepository: UserCommonRepository,
    private val jobOpeningScrapRepository: com.fone.jobOpening.domain.repository.JobOpeningScrapRepository,
    private val jobOpeningRepository: com.fone.jobOpening.domain.repository.JobOpeningRepository,
) {

    @Transactional
    suspend fun scrapJobOpening(email: String, jobOpeningId: Long) {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        jobOpeningScrapRepository.findByUserIdAndJobOpeningId(userId, jobOpeningId)?.let {
            jobOpeningScrapRepository.delete(it)

            val jobOpening =
                jobOpeningRepository.findByTypeAndId(null, jobOpeningId) ?: throw NotFoundJobOpeningException()

            jobOpening.scrapCount -= 1

            jobOpeningRepository.save(jobOpening)
            return
        }

        val jobOpening = jobOpeningRepository.findByTypeAndId(null, jobOpeningId) ?: throw NotFoundJobOpeningException()

        jobOpening.scrapCount += 1

        jobOpeningScrapRepository.save(
            com.fone.jobOpening.domain.entity.JobOpeningScrap(
                userId,
                jobOpeningId
            )
        )
        jobOpeningRepository.save(jobOpening)
    }
}
