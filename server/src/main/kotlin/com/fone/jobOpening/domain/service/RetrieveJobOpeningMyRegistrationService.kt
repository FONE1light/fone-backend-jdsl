package com.fone.jobOpening.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.jobOpening.domain.repository.JobOpeningCategoryRepository
import com.fone.jobOpening.domain.repository.JobOpeningDomainRepository
import com.fone.jobOpening.domain.repository.JobOpeningRepository
import com.fone.jobOpening.domain.repository.JobOpeningScrapRepository
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningMyRegistrationResponse
import com.fone.user.domain.repository.UserRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveJobOpeningMyRegistrationService(
    private val userRepository: UserRepository,
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
        val user = userRepository.findByNicknameOrEmail(null, email) ?: throw NotFoundUserException()
        val jobOpenings = jobOpeningRepository.findAllByUserId(pageable, user.id!!)
        val userJobOpeningScraps = jobOpeningScrapRepository.findByUserId(user.id!!)

        val jobOpeningIds = jobOpenings.map { it.id!! }.toList()
        val jobOpeningDomains = jobOpeningDomainRepository
            .findByJobOpeningIds(jobOpeningIds)
        val jobOpeningCategories = jobOpeningCategoryRepository
            .findByJobOpeningIds(jobOpeningIds)
        val jobOpeningUserIds = jobOpenings.content.map { it.userId }.toList()
        val jobOpeningUsers = userRepository.findByIds(jobOpeningUserIds).associateBy { it.id }

        return RetrieveJobOpeningMyRegistrationResponse(
            jobOpenings,
            userJobOpeningScraps,
            jobOpeningDomains,
            jobOpeningCategories,
            jobOpeningUsers
        )
    }
}
