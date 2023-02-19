package com.fone.filmone.domain.job_opening.repository

import com.fone.common.entity.DomainType
import com.fone.filmone.domain.job_opening.entity.JobOpeningDomain

interface JobOpeningDomainRepository {

    suspend fun saveAll(jobOpeningDomain: List<JobOpeningDomain>): List<JobOpeningDomain>

    suspend fun deleteByJobOpeningId(jobOpeningId: Long): Int

    suspend fun findByJobOpeningIds(jobOpeningIds: List<Long>): Map<Long, List<DomainType>>

    suspend fun findByJobOpeningId(jobOpeningId: Long): List<DomainType>
}