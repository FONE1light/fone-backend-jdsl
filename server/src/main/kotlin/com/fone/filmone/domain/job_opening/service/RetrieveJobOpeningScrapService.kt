package com.fone.filmone.domain.job_opening.service

import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.job_opening.entity.JobOpeningScrap
import com.fone.filmone.infrastructure.job_opening.JobOpeningRepository
import com.fone.filmone.infrastructure.job_opening.JobOpeningScrapRepository
import com.fone.filmone.infrastructure.user.UserRepository
import com.fone.filmone.presentation.job_opening.RetrieveJobOpeningScrapDto.RetrieveJobOpeningScrapResponse
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.kotlin.core.publisher.toMono

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
                it.toMono().awaitSingle()
                it.type == type
            }.toList() as ArrayList

        return RetrieveJobOpeningScrapResponse(jobOpenings)
    }
}