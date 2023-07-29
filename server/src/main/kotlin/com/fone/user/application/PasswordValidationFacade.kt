package com.fone.user.application

import com.fone.user.domain.service.PasswordValidationService
import com.fone.user.presentation.dto.PasswordValidationDto
import org.springframework.stereotype.Service

@Service
class PasswordValidationFacade(
    private val validationService: PasswordValidationService,
) {
    suspend fun validate(request: PasswordValidationDto.PasswordValidationRequest) =
        validationService.validateRequest(request)
}
