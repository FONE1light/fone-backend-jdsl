package com.fone.filmone.infrastructure.job_opening

import com.fone.filmone.domain.job_opening.entity.JobOpeningScrap

interface JobOpeningScrapRepository {

    suspend fun findByUserIdAndJobOpeningId(userId: Long, jobOpeningId: Long): JobOpeningScrap?

    suspend fun findByUserId(userId: Long): List<JobOpeningScrap>
}