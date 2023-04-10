package com.fone.user.application

import com.fone.user.domain.service.OauthValidationService
import com.fone.user.presentation.dto.OauthEmailDto
import org.springframework.stereotype.Service

@Service
class OauthValidationFacade(private val oauthValidationService: OauthValidationService) {

    suspend fun getEmail(request: OauthEmailDto.OauthEmailRequest) =
        oauthValidationService.getEmailResponse(request)
}
