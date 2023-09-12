package com.fone.jobOpening.domain.service

import com.fone.common.entity.Type
import com.fone.common.exception.NotFoundUserException
import com.fone.jobOpening.domain.repository.JobOpeningCategoryRepository
import com.fone.jobOpening.domain.repository.JobOpeningDomainRepository
import com.fone.jobOpening.domain.repository.JobOpeningRepository
import com.fone.jobOpening.domain.repository.JobOpeningScrapRepository
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningScrapDto.RetrieveJobOpeningScrapResponse
import com.fone.user.domain.repository.UserRepository
import kotlinx.coroutines.coroutineScope
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveJobOpeningScrapService(
    private val jobOpeningScrapRepository: JobOpeningScrapRepository,
    private val jobOpeningRepository: JobOpeningRepository,
    private val jobOpeningDomainRepository: JobOpeningDomainRepository,
    private val jobOpeningCategoryRepository: JobOpeningCategoryRepository,
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveJobOpeningScrap(
        pageable: Pageable,
        email: String,
        type: Type?,
    ): RetrieveJobOpeningScrapResponse {
        val user = userRepository.findByNicknameOrEmail(null, email) ?: throw NotFoundUserException()

        return coroutineScope {
            val jobOpenings = jobOpeningRepository.findScrapAllByUserId(pageable, user.id!!, type)
            val userJobOpeningScraps = jobOpeningScrapRepository.findByUserId(user.id!!)
            val jobOpeningIds = jobOpenings.map { it.id!! }.toList()
            val jobOpeningDomains = jobOpeningDomainRepository.findByJobOpeningIds(jobOpeningIds)
            val jobOpeningCategories = jobOpeningCategoryRepository.findByJobOpeningIds(jobOpeningIds)

            RetrieveJobOpeningScrapResponse(
                jobOpenings,
                userJobOpeningScraps,
                jobOpeningDomains,
                jobOpeningCategories,
                user.nickname,
                user.profileUrl
            )
        }
    }
}
