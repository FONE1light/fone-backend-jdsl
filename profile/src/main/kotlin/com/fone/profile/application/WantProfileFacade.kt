package com.fone.profile.application

import com.fone.profile.domain.service.WantProfileService
import org.springframework.stereotype.Service

@Service
class WantProfileFacade(
    private val wantProfileService: WantProfileService,
) {

    suspend fun wantProfile(email: String, profileId: Long) =
        wantProfileService.wantProfile(email, profileId)
}