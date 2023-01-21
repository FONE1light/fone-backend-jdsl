package com.fone.filmone.domain.job_opening.service

import com.fone.filmone.domain.common.Type
import com.fone.filmone.infrastructure.job_opening.JobOpeningRepository
import com.fone.filmone.presentation.job_opening.RetrieveJobOpeningDto.RetrieveJobOpeningsResponse
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.kotlin.core.publisher.toMono

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
        val jobOpenings = jobOpeningRepository.findByType(pageable, type.toString())
            .map { it.toMono().awaitSingle() }
            .toList()

        return RetrieveJobOpeningsResponse(jobOpenings, pageable)
    }
}