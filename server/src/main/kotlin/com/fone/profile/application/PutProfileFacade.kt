package com.fone.profile.application

import com.fone.profile.presentation.dto.RegisterProfileDto.RegisterProfileRequest
import org.springframework.stereotype.Service

@Service
class PutProfileFacade(
    private val putProfileService: com.fone.profile.domain.service.PutProfileService,
) {

    suspend fun putProfile(
        request: RegisterProfileRequest,
        email: String,
        profileId: Long,
    ) = putProfileService.putProfile(request, email, profileId)
}
