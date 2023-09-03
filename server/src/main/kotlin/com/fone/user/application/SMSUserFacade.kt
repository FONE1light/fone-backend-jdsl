package com.fone.user.application

import com.fone.sms.domain.service.AligoService
import com.fone.sms.presentation.dto.SMSSendRequest
import com.fone.user.domain.service.SMSValidationService
import com.fone.user.presentation.dto.SMSUserDto
import org.springframework.stereotype.Service

@Service
class SMSUserFacade(
    private val smsValidationService: SMSValidationService,
    private val aligoService: AligoService,
) {
    suspend fun sendSMS(request: SMSUserDto.SMSRequest): SMSUserDto.SMSResponse {
        return smsValidationService.sendSMS(request, ::smsSender)
    }

    suspend fun validatePassword(
        request: SMSUserDto.SMSValidationRequest,
    ): SMSUserDto.PasswordSMSValidationResponse {
        return smsValidationService.validateSMSPassword(request)
    }

    suspend fun validateUserInfo(request: SMSUserDto.SMSValidationRequest) =
        smsValidationService.validateSMSUserInfo(request)

    private suspend fun smsSender(recipient: String, code: String) {
        aligoService.sendToAligo(SMSSendRequest(recipient, code))
    }
}
