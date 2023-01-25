package com.fone.filmone.domain.job_opening.service

import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.domain.job_opening.entity.JobOpeningScrap
import com.fone.filmone.domain.job_opening.repository.JobOpeningScrapRepository
import com.fone.filmone.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ScrapJobOpeningService(
    private val userRepository: UserRepository,
    private val jobOpeningScrapRepository: JobOpeningScrapRepository,
) {

    @Transactional
    suspend fun scrapJobOpening(email: String, jobOpeningId: Long) {
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()

        jobOpeningScrapRepository.findByUserIdAndJobOpeningId(user.id!!, jobOpeningId)
            ?.let {
                jobOpeningScrapRepository.delete(it)
                return
            }

        val jobOpeningScrap = JobOpeningScrap(user.id!!, jobOpeningId)
        jobOpeningScrapRepository.save(jobOpeningScrap)
    }
}