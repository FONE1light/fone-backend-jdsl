package com.fone.filmone.infrastructure.profile

import com.fone.filmone.domain.profile.entity.ProfileWant
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ProfileWantRepository : CoroutineCrudRepository<ProfileWant, Long> {

    suspend fun findByUserId(userId: Long): List<ProfileWant>
    suspend fun findByUserIdAndProfileId(userId: Long, profileId: Long): ProfileWant?
}