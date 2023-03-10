package com.fone.jobOpening.domain.service

import com.fone.common.exception.InvalidJobOpeningUserIdException
import com.fone.common.exception.NotFoundJobOpeningException
import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
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
    private val userRepository: UserCommonRepository,
) {

    suspend fun putJobOpening(
        request: RegisterJobOpeningRequest,
        email: String,
        jobOpeningId: Long,
    ): RegisterJobOpeningResponse {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()
        val jobOpening = jobOpeningRepository.findByTypeAndId(null, jobOpeningId) ?: throw NotFoundJobOpeningException()
        if (userId != jobOpening.userId) {
            throw InvalidJobOpeningUserIdException()
        }

        jobOpeningDomainRepository.deleteByJobOpeningId(jobOpening.id!!)
        val jobOpeningDomains = request.domains.map {
            com.fone.jobOpening.domain.entity.JobOpeningDomain(
                jobOpening.id!!,
                it
            )
        }
        jobOpeningDomainRepository.saveAll(jobOpeningDomains)

        jobOpeningCategoryRepository.deleteByJobOpeningId(jobOpening.id!!)
        val jobOpeningCategories = request.categories.map {
            com.fone.jobOpening.domain.entity.JobOpeningCategory(
                jobOpening.id!!,
                it
            )
        }
        jobOpeningCategoryRepository.saveAll(jobOpeningCategories)

        return coroutineScope {
            val jobOpening = async {
                jobOpening.put(request)
                jobOpeningRepository.save(jobOpening)
                jobOpening
            }

            val userJobOpeningScraps = async { jobOpeningScrapRepository.findByUserId(userId) }

            RegisterJobOpeningResponse(
                jobOpening.await(),
                userJobOpeningScraps.await(),
                request.domains,
                request.categories
            )
        }
    }
}
