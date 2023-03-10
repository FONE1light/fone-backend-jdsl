package com.fone.jobOpening.domain.repository

import com.fone.common.entity.Type
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningDto.RetrieveJobOpeningsRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface JobOpeningRepository {
    suspend fun findAllTop5ByType(pageable: Pageable, type: Type): Slice<com.fone.jobOpening.domain.entity.JobOpening>

    suspend fun findByFilters(
        pageable: Pageable,
        type: RetrieveJobOpeningsRequest,
    ): Slice<com.fone.jobOpening.domain.entity.JobOpening>

    suspend fun findByTypeAndId(type: Type?, jobOpeningId: Long?): com.fone.jobOpening.domain.entity.JobOpening?

    suspend fun findAllByUserId(pageable: Pageable, userId: Long): Slice<com.fone.jobOpening.domain.entity.JobOpening>

    suspend fun findScrapAllByUserId(
        pageable: Pageable,
        userId: Long,
        type: Type,
    ): Slice<com.fone.jobOpening.domain.entity.JobOpening>

    suspend fun save(jobOpening: com.fone.jobOpening.domain.entity.JobOpening): com.fone.jobOpening.domain.entity.JobOpening
}
