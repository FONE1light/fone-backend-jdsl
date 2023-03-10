package com.fone.profile.domain.repository

import com.fone.common.entity.CategoryType

interface ProfileCategoryRepository {

    suspend fun saveAll(profileCategory: List<com.fone.profile.domain.entity.ProfileCategory>): List<com.fone.profile.domain.entity.ProfileCategory>

    suspend fun deleteByProfileId(profileId: Long): Int

    suspend fun findByProfileIds(profileIds: List<Long>): Map<Long, List<CategoryType>>

    suspend fun findByProfileId(profileId: Long): List<CategoryType>
}
