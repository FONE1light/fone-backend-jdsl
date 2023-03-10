package com.fone.jobOpening.domain.service

import com.fone.common.entity.Type
import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.jobOpening.presentation.dto.RetrieveMySimilarJobOpeningDto.RetrieveMySimilarJobOpeningResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveMySimilarJobOpeningService(
    private val jobOpeningRepository: com.fone.jobOpening.domain.repository.JobOpeningRepository,
    private val jobOpeningScrapRepository: com.fone.jobOpening.domain.repository.JobOpeningScrapRepository,
    private val jobOpeningDomainRepository: com.fone.jobOpening.domain.repository.JobOpeningDomainRepository,
    private val jobOpeningCategoryRepository: com.fone.jobOpening.domain.repository.JobOpeningCategoryRepository,
    private val userRepository: UserCommonRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveMySimilarJobOpening(
        pageable: Pageable,
        email: String,
    ): RetrieveMySimilarJobOpeningResponse {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()
        val userJob = userRepository.findJobByEmail(email) ?: throw NotFoundUserException()

        return coroutineScope {
            val userJobOpeningScraps = async { jobOpeningScrapRepository.findByUserId(userId) }

            val jobType = if (userJob == "ACTOR") "ACTOR" else "STAFF"
            val jobOpenings = jobOpeningRepository.findAllTop5ByType(pageable, Type(jobType)).content
            val jobOpeningIds = jobOpenings.map { it.id!! }.toList()
            val jobOpeningDomains = jobOpeningDomainRepository.findByJobOpeningIds(jobOpeningIds)
            val jobOpeningCategories = jobOpeningCategoryRepository.findByJobOpeningIds(jobOpeningIds)

            RetrieveMySimilarJobOpeningResponse(
                jobOpenings,
                userJobOpeningScraps.await(),
                jobOpeningDomains,
                jobOpeningCategories,
                pageable
            )
        }
    }
}
