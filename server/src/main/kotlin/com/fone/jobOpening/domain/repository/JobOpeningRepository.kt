package com.fone.jobOpening.domain.repository

import com.fone.common.entity.Type
import com.fone.jobOpening.domain.entity.JobOpening
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningDto.RetrieveJobOpeningsRequest
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
        type: Type?,
    ): Slice<JobOpening>

    suspend fun save(jobOpening: JobOpening): JobOpening
}
