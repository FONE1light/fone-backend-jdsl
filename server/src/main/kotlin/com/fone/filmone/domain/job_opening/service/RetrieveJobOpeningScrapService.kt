package com.fone.filmone.domain.job_opening.service

import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.job_opening.entity.JobOpeningScrap
import com.fone.filmone.domain.job_opening.repository.JobOpeningRepository
import com.fone.filmone.domain.job_opening.repository.JobOpeningScrapRepository
import com.fone.filmone.domain.user.repository.UserRepository
import com.fone.filmone.presentation.job_opening.RetrieveJobOpeningScrapDto.RetrieveJobOpeningScrapResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveJobOpeningScrapService(
    private val jobOpeningScrapRepository: JobOpeningScrapRepository,
    private val jobOpeningRepository: JobOpeningRepository,
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveJobOpeningScrap(
        email: String,
        type: Type,
    ): RetrieveJobOpeningScrapResponse {
        val user = userRepository.findByEmail(email) ?: throw NotFoundUserException()
        val jobOpeningScraps = jobOpeningScrapRepository.findByUserId(user.id!!)
        val jobOpeningIds = jobOpeningScraps
            .map(JobOpeningScrap::jobOpeningId)
            .toList()

        val jobOpenings = jobOpeningRepository.findAllById(jobOpeningIds)
            .filter {
                it.type == type
            }.toList() as ArrayList

        return RetrieveJobOpeningScrapResponse(jobOpenings)
    }
}