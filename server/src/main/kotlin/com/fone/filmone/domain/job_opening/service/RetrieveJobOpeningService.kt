package com.fone.filmone.domain.job_opening.service

import com.fone.common.exception.NotFoundJobOpeningException
import com.fone.common.exception.NotFoundUserException
import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.job_opening.repository.JobOpeningRepository
import com.fone.filmone.domain.job_opening.repository.JobOpeningScrapRepository
import com.fone.filmone.domain.user.repository.UserRepository
import com.fone.filmone.presentation.job_opening.RetrieveJobOpeningDto.RetrieveJobOpeningResponse
import com.fone.filmone.presentation.job_opening.RetrieveJobOpeningDto.RetrieveJobOpeningsResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveJobOpeningService(
    private val jobOpeningRepository: JobOpeningRepository,
    private val jobOpeningScrapRepository: JobOpeningScrapRepository,
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveJobOpenings(
        email: String,
        pageable: Pageable,
        type: Type,
    ): RetrieveJobOpeningsResponse {
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()

        return coroutineScope {
            val jobOpenings = async {
                jobOpeningRepository.findByType(pageable, type).content
            }

            val userJobOpeningScraps = async {
                jobOpeningScrapRepository.findByUserId(user.id!!)
            }

            RetrieveJobOpeningsResponse(jobOpenings.await(), userJobOpeningScraps.await(), pageable)
        }
    }

    @Transactional
    suspend fun retrieveJobOpening(
        email: String,
        type: Type,
        jobOpeningId: Long,
    ): RetrieveJobOpeningResponse {
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()

        return coroutineScope {
            val jobOpening = async {
                val jobOpening = jobOpeningRepository.findByTypeAndId(type, jobOpeningId)
                    ?: throw NotFoundJobOpeningException()
                jobOpening.view()
                jobOpeningRepository.save(jobOpening)
            }

            val userJobOpeningScraps = async {
                jobOpeningScrapRepository.findByUserId(user.id!!)
            }

            RetrieveJobOpeningResponse(jobOpening.await(), userJobOpeningScraps.await())
        }
    }
}