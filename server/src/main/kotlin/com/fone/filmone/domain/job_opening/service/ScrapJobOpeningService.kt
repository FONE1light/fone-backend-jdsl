package com.fone.filmone.domain.job_opening.service

import com.fone.common.exception.NotFoundJobOpeningException
import com.fone.common.exception.NotFoundUserException
import com.fone.filmone.domain.job_opening.entity.JobOpeningScrap
import com.fone.filmone.domain.job_opening.repository.JobOpeningRepository
import com.fone.filmone.domain.job_opening.repository.JobOpeningScrapRepository
import com.fone.filmone.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ScrapJobOpeningService(
    private val userRepository: UserRepository,
    private val jobOpeningScrapRepository: JobOpeningScrapRepository,
    private val jobOpeningRepository: JobOpeningRepository,
) {

    @Transactional
    suspend fun scrapJobOpening(email: String, jobOpeningId: Long) {
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()

        jobOpeningScrapRepository.findByUserIdAndJobOpeningId(user.id!!, jobOpeningId)
            ?.let {
                jobOpeningScrapRepository.delete(it)

                val jobOpening = jobOpeningRepository.findByTypeAndId(null, jobOpeningId)
                    ?: throw NotFoundJobOpeningException()

                jobOpening.scrapCount -= 1

                jobOpeningRepository.save(jobOpening)
                return
            }

        val jobOpening = jobOpeningRepository.findByTypeAndId(null, jobOpeningId)
            ?: throw NotFoundJobOpeningException()

        jobOpening.scrapCount += 1

        jobOpeningScrapRepository.save(JobOpeningScrap(user.id!!, jobOpeningId))
        jobOpeningRepository.save(jobOpening)
    }
}