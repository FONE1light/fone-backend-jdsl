package com.fone.filmone.domain.job_opening.repository

import com.fone.filmone.domain.job_opening.entity.JobOpeningScrap

interface JobOpeningScrapRepository {

    suspend fun findByUserIdAndJobOpeningId(userId: Long, jobOpeningId: Long): JobOpeningScrap?

    suspend fun findByUserId(userId: Long): List<JobOpeningScrap>

    suspend fun delete(it: JobOpeningScrap): Int

    suspend fun save(jobOpeningScrap: JobOpeningScrap): JobOpeningScrap
}