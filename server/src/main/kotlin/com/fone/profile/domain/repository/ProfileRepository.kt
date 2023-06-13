package com.fone.profile.domain.repository

import com.fone.common.entity.Type
import com.fone.profile.domain.entity.Profile
import com.fone.profile.presentation.dto.RetrieveProfilesDto.RetrieveProfilesRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProfileRepository {
    suspend fun findAllByFilters(
        pageable: Pageable,
        request: RetrieveProfilesRequest,
    ): Page<Profile>

    suspend fun findByTypeAndId(type: Type?, profileId: Long?): Profile?

    suspend fun findAllByUserId(pageable: Pageable, userId: Long): Page<Profile>
    suspend fun save(profile: Profile): Profile

    suspend fun findWantAllByUserId(
        pageable: Pageable,
        userId: Long,
        type: Type,
    ): Page<Profile>
}
