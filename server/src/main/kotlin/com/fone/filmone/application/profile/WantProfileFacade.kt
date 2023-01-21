package com.fone.filmone.application.profile

import com.fone.filmone.domain.profile.service.WantProfileService
import org.springframework.stereotype.Service

@Service
class WantProfileFacade(
    private val wantProfileService: WantProfileService,
) {

    suspend fun wantProfile(email: String, profileId: Long) =
        wantProfileService.wantProfile(email, profileId)
}