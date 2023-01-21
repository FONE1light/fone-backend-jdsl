package com.fone.filmone.application.profile

import com.fone.filmone.domain.profile.service.PutProfileService
import com.fone.filmone.presentation.profile.RegisterProfileDto
import org.springframework.stereotype.Service

@Service
class PutProfileFacade(
    private val putProfileService: PutProfileService,
) {

    suspend fun putProfile(
        request: RegisterProfileDto.RegisterProfileRequest,
        email: String,
        profileId: Long
    ) = putProfileService.putProfile(request, email, profileId)
}