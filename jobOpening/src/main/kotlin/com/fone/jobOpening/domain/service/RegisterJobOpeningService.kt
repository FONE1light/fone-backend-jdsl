package com.fone.jobOpening.domain.service

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
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterJobOpeningService(
    private val jobOpeningRepository: JobOpeningRepository,
    private val jobOpeningScrapRepository: JobOpeningScrapRepository,
    private val jobOpeningDomainRepository: JobOpeningDomainRepository,
    private val jobOpeningCategoryRepository: JobOpeningCategoryRepository,
    private val userRepository: UserCommonRepository,
) {

    @Transactional
    suspend fun registerJobOpening(
        request: RegisterJobOpeningRequest,
        email: String,
    ): RegisterJobOpeningResponse {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        return coroutineScope {
            with(request) {
                val jobOpening = async {
                    val jobOpening = toEntity(userId)
                    jobOpeningRepository.save(jobOpening)

                    val jobOpeningDomains = domains.map { JobOpeningDomain(jobOpening.id!!, it) }

                    val jobOpeningCategories =
                        categories.map { JobOpeningCategory(jobOpening.id!!, it) }
                    jobOpeningDomainRepository.saveAll(jobOpeningDomains)
                    jobOpeningCategoryRepository.saveAll(jobOpeningCategories)

                    jobOpening
                }

                val userJobOpeningScraps = async {
                    val scraps = jobOpeningScrapRepository.findByUserId(userId)
                    scraps
                }

                RegisterJobOpeningResponse(
                    jobOpening.await(),
                    userJobOpeningScraps.await(),
                    domains,
                    categories,
                )
            }
        }
    }
}
