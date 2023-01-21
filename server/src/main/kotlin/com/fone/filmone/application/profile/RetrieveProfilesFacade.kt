package com.fone.filmone.application.profile

import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.profile.service.RetrieveProfilesService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RetrieveProfilesFacade(
    private val retrieveProfilesService: RetrieveProfilesService,
) {

    suspend fun retrieveProfiles(pageable: Pageable, type: Type) =
        retrieveProfilesService.retrieveProfiles(pageable, type)

    suspend fun retrieveProfile(email: String, type: Type, profileId: Long) =
        retrieveProfilesService.retrieveProfile(email, type, profileId)
}