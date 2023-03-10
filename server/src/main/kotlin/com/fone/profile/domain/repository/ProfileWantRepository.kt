package com.fone.profile.domain.repository

import com.fone.profile.domain.entity.ProfileWant

interface ProfileWantRepository {

    suspend fun findByUserIdAndProfileId(userId: Long, profileId: Long): ProfileWant?
    suspend fun findByUserId(userId: Long): Map<Long, ProfileWant?>
    suspend fun delete(profileWant: ProfileWant): Int

    suspend fun save(profileWant: ProfileWant): ProfileWant
}
