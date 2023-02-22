package com.fone.jobOpening.domain.service

import com.fone.common.entity.Type
import com.fone.common.exception.NotFoundJobOpeningException
import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.jobOpening.domain.repository.JobOpeningCategoryRepository
import com.fone.jobOpening.domain.repository.JobOpeningDomainRepository
import com.fone.jobOpening.domain.repository.JobOpeningRepository
import com.fone.jobOpening.domain.repository.JobOpeningScrapRepository
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningDto.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveJobOpeningService(
    private val jobOpeningRepository: JobOpeningRepository,
    private val jobOpeningScrapRepository: JobOpeningScrapRepository,
    private val jobOpeningDomainRepository: JobOpeningDomainRepository,
    private val jobOpeningCategoryRepository: JobOpeningCategoryRepository,
    private val userRepository: UserCommonRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveJobOpenings(
        email: String,
        pageable: Pageable,
        request: RetrieveJobOpeningsRequest,
    ): RetrieveJobOpeningsResponse {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        return coroutineScope {
            val jobOpenings = async {
                jobOpeningRepository.findByFilters(pageable, request).content
            }

            val userJobOpeningScraps = async {
                jobOpeningScrapRepository.findByUserId(userId)
            }

            val jobOpeningIds = jobOpenings.await().map { it.id!! }.toList()
            val jobOpeningDomains = jobOpeningDomainRepository
                .findByJobOpeningIds(jobOpeningIds)
            val jobOpeningCategories = jobOpeningCategoryRepository
                .findByJobOpeningIds(jobOpeningIds)

            RetrieveJobOpeningsResponse(
                jobOpenings.await(),
                userJobOpeningScraps.await(),
                jobOpeningDomains,
                jobOpeningCategories,
                pageable
            )
        }
    }

    @Transactional
    suspend fun retrieveJobOpening(
        email: String,
        type: Type,
        jobOpeningId: Long,
    ): RetrieveJobOpeningResponse {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        return coroutineScope {
            val jobOpening = async {
                val jobOpening = jobOpeningRepository.findByTypeAndId(type, jobOpeningId)
                    ?: throw NotFoundJobOpeningException()
                jobOpening.view()
                jobOpeningRepository.save(jobOpening)
            }

            val userJobOpeningScraps = async {
                jobOpeningScrapRepository.findByUserId(userId)
            }

            val jobOpeningDomains = async {
                val jobOpeningId = jobOpening.await().id!!

                jobOpeningDomainRepository.findByJobOpeningId(jobOpeningId)
            }

            val jobOpeningCategories = async {
                val jobOpeningId = jobOpening.await().id!!

                jobOpeningCategoryRepository.findByJobOpeningId(jobOpeningId)
            }

            RetrieveJobOpeningResponse(
                jobOpening.await(),
                userJobOpeningScraps.await(),
                jobOpeningDomains.await(),
                jobOpeningCategories.await(),
            )
        }
    }
}