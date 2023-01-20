package com.fone.filmone.application.profile

import com.fone.filmone.domain.profile.service.RetrieveProfilesService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RetrieveProfilesFacade(
    private val retrieveProfilesService: RetrieveProfilesService,
) {

    suspend fun retrieveProfiles(pageable: Pageable) =
        retrieveProfilesService.retrieveProfiles(pageable)
}