package com.fone.profile.domain.repository

import com.fone.common.entity.DomainType
import com.fone.profile.domain.entity.ProfileDomain

interface ProfileDomainRepository {

    suspend fun saveAll(profileDomain: List<ProfileDomain>): List<ProfileDomain>

    suspend fun deleteByProfileId(profileId: Long): Int

    suspend fun findByProfileIds(profileIds: List<Long>): Map<Long, List<DomainType>>

    suspend fun findByProfileId(profileId: Long): List<DomainType>
}