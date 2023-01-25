package com.fone.filmone.domain.job_opening.service

import com.fone.filmone.common.exception.NotFoundJobOpeningException
import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.job_opening.repository.JobOpeningRepository
import com.fone.filmone.presentation.job_opening.RetrieveJobOpeningDto.RetrieveJobOpeningResponse
import com.fone.filmone.presentation.job_opening.RetrieveJobOpeningDto.RetrieveJobOpeningsResponse
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveJobOpeningService(
    private val jobOpeningRepository: JobOpeningRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveJobOpenings(
        email: String,
        pageable: Pageable,
        type: Type,
    ): RetrieveJobOpeningsResponse {
        val jobOpenings = jobOpeningRepository.findByType(pageable, type)

        return RetrieveJobOpeningsResponse(jobOpenings.content, pageable)
    }

    @Transactional
    suspend fun retrieveJobOpening(
        email: String,
        type: Type,
        jobOpeningId: Long,
    ): RetrieveJobOpeningResponse {
        val jobOpening = jobOpeningRepository.findByTypeAndId(type, jobOpeningId)
            ?: throw NotFoundJobOpeningException()
        jobOpening.view()
        jobOpeningRepository.save(jobOpening)

        return RetrieveJobOpeningResponse(jobOpening)
    }
}