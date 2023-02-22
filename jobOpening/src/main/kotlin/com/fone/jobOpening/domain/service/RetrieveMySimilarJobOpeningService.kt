package com.fone.jobOpening.domain.service

import com.fone.common.entity.Type
import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.jobOpening.domain.repository.JobOpeningCategoryRepository
import com.fone.jobOpening.domain.repository.JobOpeningDomainRepository
import com.fone.jobOpening.domain.repository.JobOpeningRepository
import com.fone.jobOpening.domain.repository.JobOpeningScrapRepository
import com.fone.jobOpening.presentation.dto.RetrieveMySimilarJobOpeningDto.RetrieveMySimilarJobOpeningResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveMySimilarJobOpeningService(
    private val jobOpeningRepository: JobOpeningRepository,
    private val jobOpeningScrapRepository: JobOpeningScrapRepository,
    private val jobOpeningDomainRepository: JobOpeningDomainRepository,
    private val jobOpeningCategoryRepository: JobOpeningCategoryRepository,
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
            val jobOpenings = async {
                val jobType = if (userJob == "ACTOR") "ACTOR" else "STAFF"
                jobOpeningRepository.findAllTop5ByType(pageable, Type(jobType))
            }

            val userJobOpeningScraps = async {
                jobOpeningScrapRepository.findByUserId(userId)
            }

            val jobOpeningDomains = async {
                val jobOpeningIds = jobOpenings.await().map { it.id!! }.toList()

                jobOpeningDomainRepository.findByJobOpeningIds(jobOpeningIds)
            }

            val jobOpeningCategories = async {
                val jobOpeningIds = jobOpenings.await().map { it.id!! }.toList()

                jobOpeningCategoryRepository.findByJobOpeningIds(jobOpeningIds)
            }

            RetrieveMySimilarJobOpeningResponse(
                jobOpenings.await(),
                userJobOpeningScraps.await(),
                jobOpeningDomains.await(),
                jobOpeningCategories.await(),
                pageable
            )
        }
    }
}