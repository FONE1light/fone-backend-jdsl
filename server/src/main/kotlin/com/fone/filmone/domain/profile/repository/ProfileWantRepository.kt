package com.fone.filmone.domain.profile.repository

import com.fone.filmone.domain.profile.entity.ProfileWant

interface ProfileWantRepository {

    suspend fun findByUserIdAndProfileId(userId: Long, profileId: Long): ProfileWant?
    suspend fun findByUserId(userId: Long): Map<Long, ProfileWant?>
    suspend fun delete(profileWant: ProfileWant): Int

    suspend fun save(profileWant: ProfileWant): ProfileWant
}