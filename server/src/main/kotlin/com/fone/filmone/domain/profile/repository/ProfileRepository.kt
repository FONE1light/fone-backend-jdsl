package com.fone.filmone.domain.profile.repository

import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.profile.entity.Profile
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface ProfileRepository {
    suspend fun findAllByType(pageable: Pageable, type: Type): Slice<Profile>

    suspend fun findByTypeAndId(type: Type?, profileId: Long?): Profile?

    suspend fun findAllByUserId(pageable: Pageable, userId: Long): Slice<Profile>
    suspend fun save(profile: Profile): Profile

    suspend fun findWantAllByUserId(pageable: Pageable, userId: Long, type: Type): Slice<Profile>
}