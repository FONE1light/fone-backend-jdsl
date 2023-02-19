package com.fone.filmone.domain.job_opening.service

import com.fone.common.exception.NotFoundUserException
import com.fone.common.entity.Type
import com.fone.filmone.domain.job_opening.repository.JobOpeningCategoryRepository
import com.fone.filmone.domain.job_opening.repository.JobOpeningDomainRepository
import com.fone.filmone.domain.job_opening.repository.JobOpeningRepository
import com.fone.filmone.domain.job_opening.repository.JobOpeningScrapRepository
import com.fone.user.domain.repository.UserRepository
import com.fone.filmone.presentation.job_opening.RetrieveMySimilarJobOpeningDto.RetrieveMySimilarJobOpeningResponse
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
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveMySimilarJobOpening(
        pageable: Pageable,
        email: String,
    ): RetrieveMySimilarJobOpeningResponse {
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()

        return coroutineScope {
            val jobOpenings = async {
                val jobType = if (user.job.toString() == "ACTOR") "ACTOR" else "STAFF"
                jobOpeningRepository.findAllTop5ByType(pageable, Type(jobType))
            }

            val userJobOpeningScraps = async {
                jobOpeningScrapRepository.findByUserId(user.id!!)
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