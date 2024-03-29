package com.fone.jobOpening.domain.service

import com.fone.common.exception.InvalidJobOpeningUserIdException
import com.fone.common.exception.NotFoundJobOpeningException
import com.fone.common.exception.NotFoundUserException
import com.fone.jobOpening.domain.repository.JobOpeningCategoryRepository
import com.fone.jobOpening.domain.repository.JobOpeningDomainRepository
import com.fone.jobOpening.domain.repository.JobOpeningRepository
import com.fone.jobOpening.domain.repository.JobOpeningScrapRepository
import com.fone.jobOpening.presentation.dto.RegisterJobOpeningRequest
import com.fone.jobOpening.presentation.dto.RegisterJobOpeningResponse
import com.fone.user.domain.enum.Job
import com.fone.user.domain.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class PutJobOpeningService(
    private val jobOpeningRepository: JobOpeningRepository,
    private val jobOpeningScrapRepository: JobOpeningScrapRepository,
    private val jobOpeningDomainRepository: JobOpeningDomainRepository,
    private val jobOpeningCategoryRepository: JobOpeningCategoryRepository,
    private val userRepository: UserRepository,
) {

    suspend fun putJobOpening(
        request: RegisterJobOpeningRequest,
        email: String,
        jobOpeningId: Long,
    ): RegisterJobOpeningResponse {
        val user = userRepository.findByNicknameOrEmail(null, email) ?: throw NotFoundUserException()
        val jobOpening = jobOpeningRepository.findByTypeAndId(null, jobOpeningId) ?: throw NotFoundJobOpeningException()
        if (user.id != jobOpening.userId) {
            throw InvalidJobOpeningUserIdException()
        }

        jobOpeningDomainRepository.deleteByJobOpeningId(jobOpening.id!!)
        val jobOpeningDomains = request.thirdPage.domains?.map {
            com.fone.jobOpening.domain.entity.JobOpeningDomain(
                jobOpening.id!!,
                it
            )
        }
        jobOpeningDomainRepository.saveAll(jobOpeningDomains)

        jobOpeningCategoryRepository.deleteByJobOpeningId(jobOpening.id!!)
        val jobOpeningCategories = request.secondPage.categories.map {
            com.fone.jobOpening.domain.entity.JobOpeningCategory(
                jobOpening.id!!,
                it
            )
        }
        jobOpeningCategoryRepository.saveAll(jobOpeningCategories)

        jobOpening.put(request)
        jobOpeningRepository.save(jobOpening)

        val userJobOpeningScraps = jobOpeningScrapRepository.findByUserId(user.id!!)

        val jobOpeningUser = userRepository.findById(jobOpening.userId)

        return RegisterJobOpeningResponse(
            jobOpening,
            userJobOpeningScraps,
            request.thirdPage.domains,
            request.secondPage.categories,
            jobOpeningUser?.nickname ?: "",
            jobOpeningUser?.profileUrl ?: "",
            jobOpeningUser?.job ?: Job.ACTOR,
            jobOpeningUser?.isVerified ?: false
        )
    }
}
