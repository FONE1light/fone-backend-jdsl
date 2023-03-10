package com.fone.jobOpening.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.jobOpening.domain.repository.JobOpeningCategoryRepository
import com.fone.jobOpening.domain.repository.JobOpeningDomainRepository
import com.fone.jobOpening.domain.repository.JobOpeningRepository
import com.fone.jobOpening.domain.repository.JobOpeningScrapRepository
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningMyRegistrationDto.RetrieveJobOpeningMyRegistrationResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveJobOpeningMyRegistrationService(
    private val userRepository: UserCommonRepository,
    private val jobOpeningRepository: JobOpeningRepository,
    private val jobOpeningScrapRepository: JobOpeningScrapRepository,
    private val jobOpeningDomainRepository: JobOpeningDomainRepository,
    private val jobOpeningCategoryRepository: JobOpeningCategoryRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveJobOpeningMyRegistrations(
        pageable: Pageable,
        email: String,
    ): RetrieveJobOpeningMyRegistrationResponse {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        return coroutineScope {
            val jobOpenings = async {
                jobOpeningRepository.findAllByUserId(pageable, userId).content
            }

            val userJobOpeningScraps = async { jobOpeningScrapRepository.findByUserId(userId) }

            val jobOpeningIds = jobOpenings.await().map { it.id!! }.toList()
            val jobOpeningDomains = jobOpeningDomainRepository
                .findByJobOpeningIds(jobOpeningIds)
            val jobOpeningCategories = jobOpeningCategoryRepository
                .findByJobOpeningIds(jobOpeningIds)

            RetrieveJobOpeningMyRegistrationResponse(
                jobOpenings.await(),
                userJobOpeningScraps.await(),
                jobOpeningDomains,
                jobOpeningCategories,
                pageable
            )
        }
    }
}
