package com.fone.jobOpening.domain.repository

import com.fone.common.entity.DomainType

interface JobOpeningDomainRepository {

    suspend fun saveAll(jobOpeningDomain: List<com.fone.jobOpening.domain.entity.JobOpeningDomain>): List<com.fone.jobOpening.domain.entity.JobOpeningDomain>

    suspend fun deleteByJobOpeningId(jobOpeningId: Long): Int

    suspend fun findByJobOpeningIds(jobOpeningIds: List<Long>): Map<Long, List<DomainType>>

    suspend fun findByJobOpeningId(jobOpeningId: Long): List<DomainType>
}
