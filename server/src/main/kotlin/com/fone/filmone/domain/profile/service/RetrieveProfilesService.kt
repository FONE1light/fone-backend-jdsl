package com.fone.filmone.domain.profile.service

import com.fone.filmone.common.exception.NotFoundProfileException
import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.profile.repository.ProfileRepository
import com.fone.filmone.presentation.profile.RetrieveProfilesDto.RetrieveProfileResponse
import com.fone.filmone.presentation.profile.RetrieveProfilesDto.RetrieveProfilesResponse
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveProfilesService(
    private val profileRepository: ProfileRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveProfiles(pageable: Pageable, type: Type): RetrieveProfilesResponse {
        val profiles = profileRepository.findAllByType(pageable, type)

        return RetrieveProfilesResponse(profiles.content, pageable)
    }

    @Transactional
    suspend fun retrieveProfile(
        email: String,
        type: Type,
        profileId: Long,
    ): RetrieveProfileResponse {
        val profile = profileRepository.findByTypeAndId(type, profileId)
            ?: throw NotFoundProfileException()
        profile.view()
        profileRepository.save(profile)

        return RetrieveProfileResponse(profile)
    }
}