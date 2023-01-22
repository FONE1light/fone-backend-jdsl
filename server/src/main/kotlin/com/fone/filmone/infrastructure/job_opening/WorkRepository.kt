package com.fone.filmone.infrastructure.job_opening

import com.fone.filmone.domain.job_opening.entity.Work
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface WorkRepository : CoroutineCrudRepository<Work, Long> {

    suspend fun findByJobOpeningId(jobOpening: Long): Work?
}