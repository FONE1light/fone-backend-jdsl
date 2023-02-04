package com.fone.filmone.domain.job_opening.service

import com.fone.common.exception.InvalidJobOpeningUserIdException
import com.fone.common.exception.NotFoundJobOpeningException
import com.fone.common.exception.NotFoundUserException
import com.fone.filmone.domain.job_opening.entity.JobOpeningDomain
import com.fone.filmone.domain.job_opening.repository.JobOpeningDomainRepository
import com.fone.filmone.domain.job_opening.repository.JobOpeningRepository
import com.fone.filmone.domain.job_opening.repository.JobOpeningScrapRepository
import com.fone.filmone.domain.user.repository.UserRepository
import com.fone.filmone.presentation.job_opening.RegisterJobOpeningDto.RegisterJobOpeningRequest
import com.fone.filmone.presentation.job_opening.RegisterJobOpeningDto.RegisterJobOpeningResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
class PutJobOpeningService(
    private val jobOpeningRepository: JobOpeningRepository,
    private val jobOpeningScrapRepository: JobOpeningScrapRepository,
    private val jobOpeningDomainRepository: JobOpeningDomainRepository,
    private val userRepository: UserRepository,
) {

    suspend fun putJobOpening(
        request: RegisterJobOpeningRequest,
        email: String,
        jobOpeningId: Long,
    ): RegisterJobOpeningResponse {
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()
        val jobOpening = jobOpeningRepository.findByTypeAndId(null, jobOpeningId)
            ?: throw NotFoundJobOpeningException()
        if (user.id != jobOpening.userId) {
            throw InvalidJobOpeningUserIdException()
        }

        return coroutineScope {
            val jobOpeningDomains = async {
                jobOpeningDomainRepository.deleteByJobOpeningId(jobOpening.id!!)
                val jobOpeningDomains = request.domains.map {
                    JobOpeningDomain(
                        jobOpening.id!!,
                        it
                    )
                }
                jobOpeningDomainRepository.saveAll(jobOpeningDomains)
            }

            val jobOpening = async {
                jobOpening.put(request)
                jobOpeningRepository.save(jobOpening)
            }

            val userJobOpeningScraps = async {
                jobOpeningScrapRepository.findByUserId(user.id!!)
            }

            jobOpeningDomains.await()
            RegisterJobOpeningResponse(
                jobOpening.await(),
                userJobOpeningScraps.await(),
                request.domains
            )
        }
    }
}