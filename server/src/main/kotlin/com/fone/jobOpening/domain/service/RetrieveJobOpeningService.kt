package com.fone.jobOpening.domain.service

import com.fone.common.entity.Type
import com.fone.common.exception.NotFoundJobOpeningException
import com.fone.common.exception.NotFoundUserException
import com.fone.jobOpening.domain.repository.JobOpeningCategoryRepository
import com.fone.jobOpening.domain.repository.JobOpeningDomainRepository
import com.fone.jobOpening.domain.repository.JobOpeningRepository
import com.fone.jobOpening.domain.repository.JobOpeningScrapRepository
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningDto.RetrieveJobOpeningResponse
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningDto.RetrieveJobOpeningsRequest
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningDto.RetrieveJobOpeningsResponse
import com.fone.user.domain.enum.Job
import com.fone.user.domain.repository.UserRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveJobOpeningService(
    private val jobOpeningRepository: JobOpeningRepository,
    private val jobOpeningScrapRepository: JobOpeningScrapRepository,
    private val jobOpeningDomainRepository: JobOpeningDomainRepository,
    private val jobOpeningCategoryRepository: JobOpeningCategoryRepository,
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveJobOpenings(
        email: String,
        pageable: Pageable,
        request: RetrieveJobOpeningsRequest,
    ): RetrieveJobOpeningsResponse {
        val user = userRepository.findByNicknameOrEmail(null, email) ?: throw NotFoundUserException()

        val jobOpenings = jobOpeningRepository.findByFilters(pageable, request)
        val userJobOpeningScraps = jobOpeningScrapRepository.findByUserId(user.id!!)
        val jobOpeningIds = jobOpenings.map { it.id!! }.toList()
        val jobOpeningDomains = jobOpeningDomainRepository.findByJobOpeningIds(jobOpeningIds)
        val jobOpeningCategories = jobOpeningCategoryRepository.findByJobOpeningIds(jobOpeningIds)

        return RetrieveJobOpeningsResponse(
            jobOpenings,
            userJobOpeningScraps,
            jobOpeningDomains,
            jobOpeningCategories,
            user.nickname,
            user.profileUrl
        )
    }

    @Transactional
    suspend fun retrieveJobOpening(
        email: String,
        type: Type,
        jobOpeningId: Long,
    ): RetrieveJobOpeningResponse {
        val user = userRepository.findByNicknameOrEmail(null, email) ?: throw NotFoundUserException()

        val jobOpening =
            jobOpeningRepository.findByTypeAndId(type, jobOpeningId) ?: throw NotFoundJobOpeningException()
        jobOpening.view()
        jobOpeningRepository.save(jobOpening)
        val userJobOpeningScraps = jobOpeningScrapRepository.findByUserId(user.id!!)
        val jobOpeningDomains = jobOpeningDomainRepository.findByJobOpeningId(jobOpening.id!!)
        val jobOpeningCategories = jobOpeningCategoryRepository.findByJobOpeningId(jobOpening.id!!)

        val jobOpeningUser = userRepository.findById(jobOpening.userId)

        return RetrieveJobOpeningResponse(
            jobOpening,
            userJobOpeningScraps,
            jobOpeningDomains,
            jobOpeningCategories,
            jobOpeningUser?.nickname ?: "",
            jobOpeningUser?.profileUrl ?: "",
            jobOpeningUser?.job ?: Job.ACTOR
        )
    }
}
