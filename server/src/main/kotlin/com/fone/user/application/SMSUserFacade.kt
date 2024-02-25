package com.fone.user.application

import com.fone.sms.domain.service.AligoService
import com.fone.sms.presentation.dto.SMSSendDto.SMSSendRequest
import com.fone.user.domain.service.SMSValidationService
import com.fone.user.presentation.dto.PasswordSMSValidationResponse
import com.fone.user.presentation.dto.SMSRequest
import com.fone.user.presentation.dto.SMSValidationRequest
import org.springframework.stereotype.Service

@Service
class SMSUserFacade(
    private val smsValidationService: SMSValidationService,
    private val aligoService: AligoService,
) {
    suspend fun sendSMS(request: SMSRequest) {
        return smsValidationService.sendSMS(request, ::smsSender)
    }

    suspend fun validatePassword(
        request: SMSValidationRequest,
    ): PasswordSMSValidationResponse {
        return smsValidationService.validateSMSPassword(request)
    }

    suspend fun validateUserInfo(request: SMSValidationRequest) =
        smsValidationService.validateSMSUserInfo(request)

    private suspend fun smsSender(recipient: String, code: String) {
        aligoService.sendToAligo(SMSSendRequest(recipient, code))
    }
}
