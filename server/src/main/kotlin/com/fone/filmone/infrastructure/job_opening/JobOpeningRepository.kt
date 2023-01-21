package com.fone.filmone.infrastructure.job_opening

import com.fone.filmone.domain.job_opening.entity.JobOpening
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface JobOpeningRepository : CoroutineCrudRepository<JobOpening, Long> {
    suspend fun findTop5ByType(type: String): ArrayList<JobOpening>

    fun findByType(pageable: Pageable, type: String): Flow<JobOpening>

    suspend fun findByType(type: String): JobOpening?
}