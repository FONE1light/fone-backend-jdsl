package com.fone.jobOpening.domain.repository

interface JobOpeningScrapRepository {

    suspend fun findByUserIdAndJobOpeningId(
        userId: Long,
        jobOpeningId: Long,
    ): com.fone.jobOpening.domain.entity.JobOpeningScrap?

    suspend fun findByUserId(userId: Long): Map<Long, com.fone.jobOpening.domain.entity.JobOpeningScrap?>

    suspend fun delete(it: com.fone.jobOpening.domain.entity.JobOpeningScrap): Int

    suspend fun save(jobOpeningScrap: com.fone.jobOpening.domain.entity.JobOpeningScrap): com.fone.jobOpening.domain.entity.JobOpeningScrap
}
