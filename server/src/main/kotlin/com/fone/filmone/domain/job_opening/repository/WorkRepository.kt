package com.fone.filmone.domain.job_opening.repository

import com.fone.filmone.domain.job_opening.entity.Work

interface WorkRepository {

    suspend fun findByJobOpeningId(jobOpeningId: Long): Work?
    suspend fun save(work: Work): Work
}