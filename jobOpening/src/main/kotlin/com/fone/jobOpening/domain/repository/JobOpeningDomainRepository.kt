package com.fone.jobOpening.domain.repository

import com.fone.common.entity.DomainType
import com.fone.jobOpening.domain.entity.JobOpeningDomain

interface JobOpeningDomainRepository {

    suspend fun saveAll(jobOpeningDomain: List<JobOpeningDomain>): List<JobOpeningDomain>

    suspend fun deleteByJobOpeningId(jobOpeningId: Long): Int

    suspend fun findByJobOpeningIds(jobOpeningIds: List<Long>): Map<Long, List<DomainType>>

    suspend fun findByJobOpeningId(jobOpeningId: Long): List<DomainType>
}