package com.fone.filmone.infrastructure.job_opening

import com.fone.filmone.domain.job_opening.entity.JobOpeningScrap
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface JobOpeningScrapRepository : CoroutineCrudRepository<JobOpeningScrap, Long> {

    suspend fun findByUserIdAndJobOpeningId(userId: Long, jobOpeningId: Long): JobOpeningScrap?
}