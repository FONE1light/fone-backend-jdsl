package com.fone.jobOpening.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.jobOpening.domain.repository.JobOpeningCategoryRepository
import com.fone.jobOpening.domain.repository.JobOpeningDomainRepository
import com.fone.jobOpening.domain.repository.JobOpeningRepository
import com.fone.jobOpening.domain.repository.JobOpeningScrapRepository
import com.fone.jobOpening.presentation.dto.RegisterJobOpeningDto.RegisterJobOpeningRequest
import com.fone.jobOpening.presentation.dto.RegisterJobOpeningDto.RegisterJobOpeningResponse
import com.fone.user.domain.enum.Job
import com.fone.user.domain.repository.UserRepository
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
        val user = userRepository.findByNicknameOrEmail(null, email) ?: throw NotFoundUserException()
        return with(request) {
            val jobOpening = toEntity(user.id!!)
            jobOpeningRepository.save(jobOpening)

            val jobOpeningDomains = domains?.map {
                com.fone.jobOpening.domain.entity.JobOpeningDomain(
                    jobOpening.id!!,
                    it
                )
            }
            val jobOpeningCategories = categories.map {
                com.fone.jobOpening.domain.entity.JobOpeningCategory(
                    jobOpening.id!!,
                    it
                )
            }
            jobOpeningDomainRepository.saveAll(jobOpeningDomains)
            jobOpeningCategoryRepository.saveAll(jobOpeningCategories)
            val scraps = jobOpeningScrapRepository.findByUserId(user.id!!)

            val jobOpeningUser = userRepository.findById(jobOpening.userId)
            RegisterJobOpeningResponse(
                jobOpening,
                scraps,
                domains,
                categories,
                jobOpeningUser?.nickname ?: "",
                jobOpeningUser?.profileUrl ?: "",
                jobOpeningUser?.job ?: Job.ACTOR
            )
        }
    }
}
