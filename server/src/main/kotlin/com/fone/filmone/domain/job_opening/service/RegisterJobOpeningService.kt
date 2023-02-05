package com.fone.filmone.domain.job_opening.service

import com.fone.common.exception.NotFoundUserException
import com.fone.filmone.domain.job_opening.entity.JobOpeningCategory
import com.fone.filmone.domain.job_opening.entity.JobOpeningDomain
import com.fone.filmone.domain.job_opening.repository.JobOpeningCategoryRepository
import com.fone.filmone.domain.job_opening.repository.JobOpeningDomainRepository
import com.fone.filmone.domain.job_opening.repository.JobOpeningRepository
import com.fone.filmone.domain.job_opening.repository.JobOpeningScrapRepository
import com.fone.filmone.domain.user.repository.UserRepository
import com.fone.filmone.presentation.job_opening.RegisterJobOpeningDto.RegisterJobOpeningRequest
import com.fone.filmone.presentation.job_opening.RegisterJobOpeningDto.RegisterJobOpeningResponse
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
    private val userRepository: UserRepository,
) {

    @Transactional
    suspend fun registerJobOpening(
        request: RegisterJobOpeningRequest,
        email: String,
    ): RegisterJobOpeningResponse {
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()

        return coroutineScope {
            with(request) {
                val jobOpening = async {
                    val jobOpening = toEntity(user.id!!)
                    jobOpeningRepository.save(jobOpening)

                    val jobOpeningDomains = domains.map {
                        JobOpeningDomain(
                            jobOpening.id!!,
                            it
                        )
                    }

                    val jobOpeningCategories = categories.map {
                        JobOpeningCategory(
                            jobOpening.id!!,
                            it
                        )
                    }
                    jobOpeningDomainRepository.saveAll(jobOpeningDomains)
                    jobOpeningCategoryRepository.saveAll(jobOpeningCategories)

                    jobOpening
                }

                val userJobOpeningScraps = async {
                    val scraps = jobOpeningScrapRepository.findByUserId(user.id!!)
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