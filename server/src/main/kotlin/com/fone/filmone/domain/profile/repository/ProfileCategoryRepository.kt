package com.fone.filmone.domain.profile.repository

import com.fone.common.entity.CategoryType
import com.fone.filmone.domain.profile.entity.ProfileCategory

interface ProfileCategoryRepository {

    suspend fun saveAll(profileCategory: List<ProfileCategory>): List<ProfileCategory>

    suspend fun deleteByProfileId(profileId: Long): Int

    suspend fun findByProfileIds(profileIds: List<Long>): Map<Long, List<CategoryType>>

    suspend fun findByProfileId(profileId: Long): List<CategoryType>
}