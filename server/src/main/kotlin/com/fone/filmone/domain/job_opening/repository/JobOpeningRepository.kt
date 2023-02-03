package com.fone.filmone.domain.job_opening.repository

import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.job_opening.entity.JobOpening
import com.fone.filmone.presentation.job_opening.RetrieveJobOpeningDto.RetrieveJobOpeningsRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface JobOpeningRepository {
    suspend fun findAllTop5ByType(pageable: Pageable, type: Type): Slice<JobOpening>

    suspend fun findByFilters(
        pageable: Pageable,
        type: RetrieveJobOpeningsRequest,
    ): Slice<JobOpening>

    suspend fun findByTypeAndId(type: Type?, jobOpeningId: Long?): JobOpening?

    suspend fun findAllByUserId(pageable: Pageable, userId: Long): Slice<JobOpening>

    suspend fun findScrapAllByUserId(
        pageable: Pageable,
        userId: Long,
        type: Type,
    ): Slice<JobOpening>

    suspend fun save(jobOpening: JobOpening): JobOpening
}