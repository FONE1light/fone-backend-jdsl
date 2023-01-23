package com.fone.filmone.domain.profile.repository

import com.fone.filmone.domain.profile.entity.Profile
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

interface ProfileRepository {
    fun findByType(pageable: Pageable, type: String): Flow<Profile>

    suspend fun findByType(type: String): Profile?

    suspend fun findByUserId(userId: Long): ArrayList<Profile>
}