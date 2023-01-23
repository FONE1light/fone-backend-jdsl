package com.fone.filmone.infrastructure.job_opening

import com.fone.filmone.domain.job_opening.entity.Work

interface WorkRepository {

    suspend fun findByJobOpeningId(jobOpening: Long): Work?
}