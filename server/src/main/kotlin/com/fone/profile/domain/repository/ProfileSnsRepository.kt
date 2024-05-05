package com.fone.profile.domain.repository

import com.fone.profile.domain.entity.ProfileSns

interface ProfileSnsRepository {
    suspend fun saveAll(urls: Set<ProfileSns>): Set<ProfileSns>

    suspend fun findAll(profileId: Long): Set<ProfileSns>

    suspend fun deleteByProfileId(profileId: Long): Int
}
