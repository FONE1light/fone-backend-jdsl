package com.fone.filmone.infrastructure.profile

import com.fone.filmone.domain.profile.entity.ProfileWant

interface ProfileWantRepository {

    suspend fun findByUserId(userId: Long): List<ProfileWant>
    suspend fun findByUserIdAndProfileId(userId: Long, profileId: Long): ProfileWant?
}