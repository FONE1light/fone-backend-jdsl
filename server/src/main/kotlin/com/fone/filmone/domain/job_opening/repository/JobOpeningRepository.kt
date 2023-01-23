package com.fone.filmone.domain.job_opening.repository

import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.job_opening.entity.JobOpening
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface JobOpeningRepository {
    suspend fun findTop5ByType(type: Type): List<JobOpening>

    suspend fun findByType(pageable: Pageable, type: Type): Slice<JobOpening>

    suspend fun findByType(type: Type): JobOpening?

    suspend fun findByUserId(userId: Long): List<JobOpening>
    suspend fun findById(jobOpeningId: Long): JobOpening?

    suspend fun save(jobOpening: JobOpening): JobOpening

    suspend fun findAllById(jobOpeningIds: List<Long>): List<JobOpening>
}