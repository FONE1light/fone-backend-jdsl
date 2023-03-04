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
    private val userRepository: UserCommonRepository,
) {

    suspend fun putJobOpening(
        request: RegisterJobOpeningRequest,
        email: String,
        jobOpeningId: Long,
    ): RegisterJobOpeningResponse {
        println("test..123")
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()
        val jobOpening = jobOpeningRepository.findByTypeAndId(null, jobOpeningId) ?: throw NotFoundJobOpeningException()
        if (userId != jobOpening.userId) {
            throw InvalidJobOpeningUserIdException()
        }

        println("test..1231")
        return coroutineScope {
            val jobOpeningDomains = async {
                println("doamin test..1231 start")
                jobOpeningDomainRepository.deleteByJobOpeningId(jobOpening.id!!)
                val jobOpeningDomains = request.domains.map { JobOpeningDomain(jobOpening.id!!, it) }
                jobOpeningDomainRepository.saveAll(jobOpeningDomains)
                println("doamin test..1231 end")
            }

            val jobOpeningCategories = async {
                println("category test..1231 start")
                jobOpeningCategoryRepository.deleteByJobOpeningId(jobOpening.id!!)
                val jobOpeningCategories = request.categories.map { JobOpeningCategory(jobOpening.id!!, it) }
                println("category test..1231 end")
            }

            val jobOpening = async {
                println("save test.. start")
                jobOpening.put(request)
                println(jobOpeningId.toString() + "test11151.." + jobOpening.id)
                jobOpeningRepository.save(jobOpening)

                println("save test.. end")
                jobOpening
            }

            val userJobOpeningScraps = async { jobOpeningScrapRepository.findByUserId(userId) }

            jobOpeningDomains.await()
            jobOpeningCategories.await()

            RegisterJobOpeningResponse(
                jobOpening.await(),
                userJobOpeningScraps.await(),
                request.domains,
                request.categories
            )
        }
    }
}
