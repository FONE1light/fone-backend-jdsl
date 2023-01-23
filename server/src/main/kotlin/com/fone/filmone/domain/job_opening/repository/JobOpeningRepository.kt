package com.fone.filmone.domain.job_opening.repository

import com.fone.filmone.domain.job_opening.entity.JobOpening
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

interface JobOpeningRepository {
    suspend fun findTop5ByType(type: String): ArrayList<JobOpening>

    fun findByType(pageable: Pageable, type: String): Flow<JobOpening>

    suspend fun findByType(type: String): JobOpening?

    suspend fun findByUserId(userId: Long): ArrayList<JobOpening>
}