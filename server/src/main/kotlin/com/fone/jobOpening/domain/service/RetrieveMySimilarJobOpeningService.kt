package com.fone.jobOpening.domain.service

import com.fone.common.entity.Type
import com.fone.common.exception.NotFoundUserException
import com.fone.jobOpening.domain.repository.JobOpeningCategoryRepository
import com.fone.jobOpening.domain.repository.JobOpeningDomainRepository
import com.fone.jobOpening.domain.repository.JobOpeningRepository
import com.fone.jobOpening.domain.repository.JobOpeningScrapRepository
import com.fone.jobOpening.presentation.dto.RetrieveMySimilarJobOpeningResponse
import com.fone.user.domain.enum.Job
import com.fone.user.domain.repository.UserRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveMySimilarJobOpeningService(
    private val jobOpeningRepository: JobOpeningRepository,
    private val jobOpeningScrapRepository: JobOpeningScrapRepository,
    private val jobOpeningDomainRepository: JobOpeningDomainRepository,
    private val jobOpeningCategoryRepository: JobOpeningCategoryRepository,
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveMySimilarJobOpening(
        pageable: Pageable,
        email: String,
    ): RetrieveMySimilarJobOpeningResponse {
        val user = userRepository.findByNicknameOrEmail(null, email) ?: throw NotFoundUserException()

        val userJobOpeningScraps = jobOpeningScrapRepository.findByUserId(user.id!!)

        val jobType = when (user.job) {
            Job.ACTOR, Job.HUNTER -> "ACTOR"
            Job.STAFF, Job.NORMAL -> "STAFF"
        }
        val jobOpenings = jobOpeningRepository.findAllTop5ByType(pageable, Type(jobType))
        val jobOpeningIds = jobOpenings.map { it.id!! }.toList()
        val jobOpeningDomains = jobOpeningDomainRepository.findByJobOpeningIds(jobOpeningIds)
        val jobOpeningCategories = jobOpeningCategoryRepository.findByJobOpeningIds(jobOpeningIds)
        val jobOpeningUserIds = jobOpenings.content.map { it.userId }.toList()
        val jobOpeningUsers = userRepository.findByIds(jobOpeningUserIds).associateBy { it.id }

        return RetrieveMySimilarJobOpeningResponse(
            jobOpenings,
            userJobOpeningScraps,
            jobOpeningDomains,
            jobOpeningCategories,
            jobOpeningUsers
        )
    }
}
