package com.fone.profile.domain.repository

interface ProfileWantRepository {

    suspend fun findByUserIdAndProfileId(userId: Long, profileId: Long): com.fone.profile.domain.entity.ProfileWant?
    suspend fun findByUserId(userId: Long): Map<Long, com.fone.profile.domain.entity.ProfileWant?>
    suspend fun delete(profileWant: com.fone.profile.domain.entity.ProfileWant): Int

    suspend fun save(profileWant: com.fone.profile.domain.entity.ProfileWant): com.fone.profile.domain.entity.ProfileWant
}
