package com.fone.filmone.domain.profile.repository

import com.fone.filmone.domain.common.DomainType
import com.fone.filmone.domain.profile.entity.ProfileDomain

interface ProfileDomainRepository {

    suspend fun saveAll(profileDomain: List<ProfileDomain>): List<ProfileDomain>

    suspend fun deleteByProfileId(profileId: Long): Int

    suspend fun findByProfileIds(profileIds: List<Long>): Map<Long, List<DomainType>>

    suspend fun findByProfileId(profileId: Long): List<DomainType>
}