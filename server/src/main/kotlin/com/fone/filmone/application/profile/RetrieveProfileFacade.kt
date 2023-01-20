package com.fone.filmone.application.profile

import com.fone.filmone.domain.profile.service.RetrieveProfileService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RetrieveProfileFacade(
    private val retrieveProfileService: RetrieveProfileService,
) {

    suspend fun retrieveProfile(pageable: Pageable) =
        retrieveProfileService.retrieveProfile(pageable)
}