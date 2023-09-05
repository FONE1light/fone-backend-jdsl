package com.fone.sms.application

import com.fone.sms.domain.service.AligoService
import com.fone.sms.presentation.dto.SMSSendDto.SMSSendRequest
import com.fone.sms.presentation.dto.SMSSendDto.SMSSendResponse
import org.springframework.stereotype.Service

@Service
class SmsFacade(
    private val aligoService: AligoService,
) {
    suspend fun sendSmsMessage(request: SMSSendRequest): SMSSendResponse {
        return aligoService.sendToAligo(request)
    }
}
