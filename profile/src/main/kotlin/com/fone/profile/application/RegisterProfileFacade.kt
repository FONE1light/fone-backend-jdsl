package com.fone.profile.application

import com.fone.profile.domain.service.RegisterProfileService
import com.fone.profile.presentation.dto.RegisterProfileDto.RegisterProfileRequest
import org.springframework.stereotype.Service

@Service
class RegisterProfileFacade(
    private val registerProfileService: RegisterProfileService
) {

    suspend fun registerProfile(
        request: RegisterProfileRequest,
        email: String
    ) = registerProfileService.registerProfile(request, email)
}
