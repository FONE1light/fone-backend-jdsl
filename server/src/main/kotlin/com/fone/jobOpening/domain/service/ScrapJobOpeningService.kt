package com.fone.jobOpening.domain.service

import com.fone.common.exception.NotFoundJobOpeningException
import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.jobOpening.domain.repository.JobOpeningRepository
import com.fone.jobOpening.domain.repository.JobOpeningScrapRepository
import com.fone.jobOpening.presentation.dto.ScrapJobOpeningDto.ScrapJobOpeningResponse
import com.fone.jobOpening.presentation.dto.ScrapJobOpeningDto.ScrapJobResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ScrapJobOpeningService(
    private val userRepository: UserCommonRepository,
    private val jobOpeningScrapRepository: JobOpeningScrapRepository,
    private val jobOpeningRepository: JobOpeningRepository,
) {

    @Transactional
    suspend fun scrapJobOpening(email: String, jobOpeningId: Long): ScrapJobOpeningResponse {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        jobOpeningScrapRepository.findByUserIdAndJobOpeningId(userId, jobOpeningId)?.let {
            jobOpeningScrapRepository.delete(it)

            val jobOpening =
                jobOpeningRepository.findByTypeAndId(null, jobOpeningId) ?: throw NotFoundJobOpeningException()

            jobOpening.scrapCount -= 1

            jobOpeningRepository.save(jobOpening)
            return ScrapJobOpeningResponse(ScrapJobResult.DISCARDED)
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
        return ScrapJobOpeningResponse(ScrapJobResult.SCRAPPED)
    }
}
