package com.fone.filmone.domain.profile.repository

import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.profile.entity.Profile
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface ProfileRepository {
    suspend fun findByType(pageable: Pageable, type: Type): Slice<Profile>

    suspend fun findByType(type: Type): Profile?

    suspend fun findByUserId(userId: Long): List<Profile>
    suspend fun findById(profileId: Long): Profile?
    suspend fun save(profile: Profile): Profile

    suspend fun findAllById(profileIds: List<Long>): List<Profile>
}