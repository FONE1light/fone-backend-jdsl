package com.fone.jobOpening.domain.repository

import com.fone.common.entity.Type
import com.fone.jobOpening.domain.entity.JobOpening
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningDto.RetrieveJobOpeningsRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface JobOpeningRepository {
    suspend fun findAllTop5ByType(pageable: Pageable, type: Type): Page<JobOpening>

    suspend fun findByFilters(
        pageable: Pageable,
        type: RetrieveJobOpeningsRequest,
    ): Page<JobOpening>

    suspend fun findByTypeAndId(type: Type?, jobOpeningId: Long?): JobOpening?

    suspend fun findAllByUserId(pageable: Pageable, userId: Long): Page<JobOpening>

    suspend fun findScrapAllByUserId(
        pageable: Pageable,
        userId: Long,
        type: Type?,
    ): Page<JobOpening>

    suspend fun save(jobOpening: JobOpening): JobOpening
}
