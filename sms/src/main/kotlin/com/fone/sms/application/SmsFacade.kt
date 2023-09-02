package com.fone.sms.application

import com.fone.sms.domain.data.AligoSmsRequest
import com.fone.sms.domain.data.AligoSmsResponse
import com.fone.sms.domain.service.AligoService
import com.fone.sms.presentation.data.Result
import com.fone.sms.presentation.data.SMSVerificationRequest
import com.fone.sms.presentation.data.SmsResponseData
import com.fone.sms.presentation.data.SmsVerificationResponse
import org.springframework.stereotype.Service

@Service
class SmsFacade(
    private val aligoService: AligoService,
) {
    suspend fun sendSmsMessage(request: SMSVerificationRequest): SmsVerificationResponse {
        val aligoResponse = aligoService.sendToAligo(
            AligoSmsRequest(receiver = request.phone, msg = "인증번호는 ${request.code} 입니다")
        )
        return aligoResponse.toSmsResponse()
    }

    private fun AligoSmsResponse.toSmsResponse(): SmsVerificationResponse {
        return if ((this.errorCnt ?: 0) > 0 || resultCode < 0) {
            SmsVerificationResponse(Result.FAILURE, SmsResponseData(msgId ?: ""), message, "$resultCode")
        } else {
            SmsVerificationResponse(Result.SUCCESS, SmsResponseData(msgId ?: ""), "인증번호를 전송하였습니다.")
        }
    }
}
