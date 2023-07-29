package com.fone.jobOpening.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.jobOpening.domain.repository.JobOpeningCategoryRepository
import com.fone.jobOpening.domain.repository.JobOpeningDomainRepository
import com.fone.jobOpening.domain.repository.JobOpeningRepository
import com.fone.jobOpening.domain.repository.JobOpeningScrapRepository
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningMyRegistrationDto.RetrieveJobOpeningMyRegistrationResponse
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
        val jobOpenings = jobOpeningRepository.findAllByUserId(pageable, userId)
        val userJobOpeningScraps = jobOpeningScrapRepository.findByUserId(userId)

        val jobOpeningIds = jobOpenings.map { it.id!! }.toList()
        val jobOpeningDomains = jobOpeningDomainRepository
            .findByJobOpeningIds(jobOpeningIds)
        val jobOpeningCategories = jobOpeningCategoryRepository
            .findByJobOpeningIds(jobOpeningIds)

        return RetrieveJobOpeningMyRegistrationResponse(
            jobOpenings,
            userJobOpeningScraps,
            jobOpeningDomains,
            jobOpeningCategories
        )
    }
}
