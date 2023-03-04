package com.fone.jobOpening.domain.service

import com.fone.common.exception.InvalidJobOpeningUserIdException
import com.fone.common.exception.NotFoundJobOpeningException
import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.jobOpening.domain.entity.JobOpeningCategory
import com.fone.jobOpening.domain.entity.JobOpeningDomain
import com.fone.jobOpening.domain.repository.JobOpeningCategoryRepository
import com.fone.jobOpening.domain.repository.JobOpeningDomainRepository
import com.fone.jobOpening.domain.repository.JobOpeningRepository
import com.fone.jobOpening.domain.repository.JobOpeningScrapRepository
import com.fone.jobOpening.presentation.dto.RegisterJobOpeningDto.RegisterJobOpeningRequest
import com.fone.jobOpening.presentation.dto.RegisterJobOpeningDto.RegisterJobOpeningResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
class PutJobOpeningService(
    private val jobOpeningRepository: JobOpeningRepository,
    private val jobOpeningScrapRepository: JobOpeningScrapRepository,
    private val jobOpeningDomainRepository: JobOpeningDomainRepository,
    private val jobOpeningCategoryRepository: JobOpeningCategoryRepository,
    private val userRepository: UserCommonRepository
) {

    suspend fun putJobOpening(
        request: RegisterJobOpeningRequest,
        email: String,
        jobOpeningId: Long
    ): RegisterJobOpeningResponse {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()
        val jobOpening =
            jobOpeningRepository.findByTypeAndId(null, jobOpeningId)
                ?: throw NotFoundJobOpeningException()
        if (userId != jobOpening.userId) {
            throw InvalidJobOpeningUserIdException()
        }

        return coroutineScope {
            val jobOpeningDomains = async {
                jobOpeningDomainRepository.deleteByJobOpeningId(jobOpening.id!!)
                val jobOpeningDomains =
                    request.domains.map { JobOpeningDomain(jobOpening.id!!, it) }
                jobOpeningDomainRepository.saveAll(jobOpeningDomains)
            }

            val jobOpeningCategories = async {
                jobOpeningCategoryRepository.deleteByJobOpeningId(jobOpening.id!!)
                request.categories.map { JobOpeningCategory(jobOpening.id!!, it) }
            }

            val jo = async {
                jobOpening.put(request)
                jobOpeningRepository.save(jobOpening)
            }

            val userJobOpeningScraps = async { jobOpeningScrapRepository.findByUserId(userId) }

            jobOpeningDomains.await()
            jobOpeningCategories.await()

            RegisterJobOpeningResponse(
                jo.await(),
                userJobOpeningScraps.await(),
                request.domains,
                request.categories
            )
        }
    }
}
