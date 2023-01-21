package com.fone.filmone.infrastructure.profile

import com.fone.filmone.domain.profile.entity.Profile
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ProfileRepository : CoroutineCrudRepository<Profile, Long> {
    fun findByType(pageable: Pageable, type: String): Flow<Profile>

    suspend fun findByType(type: String): Profile?
}