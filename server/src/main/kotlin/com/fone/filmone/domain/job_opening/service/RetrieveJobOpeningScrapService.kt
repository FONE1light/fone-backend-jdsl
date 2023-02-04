package com.fone.filmone.domain.job_opening.service

import com.fone.common.exception.NotFoundUserException
import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.job_opening.repository.JobOpeningDomainRepository
import com.fone.filmone.domain.job_opening.repository.JobOpeningRepository
import com.fone.filmone.domain.job_opening.repository.JobOpeningScrapRepository
import com.fone.filmone.domain.user.repository.UserRepository
import com.fone.filmone.presentation.job_opening.RetrieveJobOpeningScrapDto.RetrieveJobOpeningScrapResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveJobOpeningScrapService(
    private val jobOpeningScrapRepository: JobOpeningScrapRepository,
    private val jobOpeningRepository: JobOpeningRepository,
    private val jobOpeningDomainRepository: JobOpeningDomainRepository,
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveJobOpeningScrap(
        pageable: Pageable,
        email: String,
        type: Type,
    ): RetrieveJobOpeningScrapResponse {
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()

        return coroutineScope {
            val jobOpenings = async {
                jobOpeningRepository.findScrapAllByUserId(pageable, user.id!!, type).content
            }

            val userJobOpeningScraps = async {
                jobOpeningScrapRepository.findByUserId(user.id!!)
            }

            val jobOpeningDomains = async {
                val jobOpeningIds = jobOpenings.await().map { it.id!! }.toList()

                jobOpeningDomainRepository.findByJobOpeningIds(jobOpeningIds)
            }


            RetrieveJobOpeningScrapResponse(
                jobOpenings.await(),
                userJobOpeningScraps.await(),
                jobOpeningDomains.await(),
                pageable
            )
        }
    }
}