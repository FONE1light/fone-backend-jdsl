package com.fone.jobOpening.domain.service

import com.fone.common.entity.Type
import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningScrapDto.RetrieveJobOpeningScrapResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveJobOpeningScrapService(
    private val jobOpeningScrapRepository: com.fone.jobOpening.domain.repository.JobOpeningScrapRepository,
    private val jobOpeningRepository: com.fone.jobOpening.domain.repository.JobOpeningRepository,
    private val jobOpeningDomainRepository: com.fone.jobOpening.domain.repository.JobOpeningDomainRepository,
    private val jobOpeningCategoryRepository: com.fone.jobOpening.domain.repository.JobOpeningCategoryRepository,
    private val userRepository: UserCommonRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveJobOpeningScrap(
        pageable: Pageable,
        email: String,
        type: Type,
    ): RetrieveJobOpeningScrapResponse {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        return coroutineScope {
            val jobOpenings = async {
                jobOpeningRepository.findScrapAllByUserId(pageable, userId, type).content
            }

            val userJobOpeningScraps = async { jobOpeningScrapRepository.findByUserId(userId) }

            val jobOpeningIds = jobOpenings.await().map { it.id!! }.toList()
            val jobOpeningDomains = jobOpeningDomainRepository.findByJobOpeningIds(jobOpeningIds)
            val jobOpeningCategories = jobOpeningCategoryRepository.findByJobOpeningIds(jobOpeningIds)

            RetrieveJobOpeningScrapResponse(
                jobOpenings.await(),
                userJobOpeningScraps.await(),
                jobOpeningDomains,
                jobOpeningCategories,
                pageable
            )
        }
    }
}
