package com.fone.jobOpening.domain.repository

import com.fone.jobOpening.domain.entity.JobOpeningScrap

interface JobOpeningScrapRepository {

    suspend fun findByUserIdAndJobOpeningId(
        userId: Long,
        jobOpeningId: Long,
    ): JobOpeningScrap?

    suspend fun findByUserId(userId: Long): Map<Long, JobOpeningScrap?>

    suspend fun delete(it: JobOpeningScrap): Int

    suspend fun save(jobOpeningScrap: JobOpeningScrap): JobOpeningScrap
}
