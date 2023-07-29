package com.fone.user.application

import com.fone.user.domain.service.SMSService
import com.fone.user.presentation.dto.SMSUserDto
import org.springframework.stereotype.Service

@Service
class SMSUserFacade(
    private val smsService: SMSService,
) {
    suspend fun sendSMS(request: SMSUserDto.SMSRequest) =
        smsService.sendSMS(request)

    suspend fun validatePassword(request: SMSUserDto.SMSValidationRequest) =
        smsService.validateSMSPassword(request)

    suspend fun validateUserInfo(request: SMSUserDto.SMSValidationRequest) =
        smsService.validateSMSUserInfo(request)
}
