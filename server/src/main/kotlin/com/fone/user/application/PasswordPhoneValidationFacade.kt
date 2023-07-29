package com.fone.user.application

import com.fone.user.domain.service.PasswordUpdateService
import com.fone.user.presentation.dto.PasswordPhoneDto
import org.springframework.stereotype.Service

@Service
class PasswordPhoneValidationFacade(
    private val passwordUpdateService: PasswordUpdateService,
) {
    suspend fun sendSMS(request: PasswordPhoneDto.PasswordPhoneSMSRequest) =
        passwordUpdateService.sendSMS(request)
    suspend fun validate(request: PasswordPhoneDto.PasswordPhoneValidationRequest) =
        passwordUpdateService.validateSMS(request)
}
