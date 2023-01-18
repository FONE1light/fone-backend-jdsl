package com.fone.filmone.application.profile

import com.fone.filmone.domain.profile.service.RegisterProfileService
import com.fone.filmone.presentation.profile.RegisterProfileDto.RegisterProfileRequest
import org.springframework.stereotype.Service

@Service
class RegisterProfileFacade(
    private val registerProfileService: RegisterProfileService,
) {

    suspend fun registerProfile(
        request: RegisterProfileRequest,
        email: String
    ) = registerProfileService.registerProfile(request, email)
}