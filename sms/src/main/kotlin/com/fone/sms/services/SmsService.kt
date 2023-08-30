package com.fone.sms.services

import com.fone.sms.data.AligoSmsRequest
import com.fone.sms.data.AligoSmsResponse
import com.fone.sms.presentation.data.Result
import com.fone.sms.presentation.data.SmsRequest
import com.fone.sms.presentation.data.SmsResponse
import com.fone.sms.presentation.data.SmsResponseData
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SmsService(
    @Value("\${credentials.sms.key}") private val key: String,
    @Value("\${credentials.sms.user-id}") private val userId: String,
    @Value("\${credentials.sms.sender}") private val sender: String,
    private val aligoService: AligoService,
) {
    suspend fun sendSmsMessage(request: SmsRequest): SmsResponse {
        val aligoResponse = aligoService.sendToAligo(
            AligoSmsRequest(key, userId, sender, request.phone, "인증번호는 ${request.code} 입니다")
        )
        return aligoResponse.toSmsResponse()
    }

    private fun AligoSmsResponse.toSmsResponse(): SmsResponse {
        return if ((this.errorCnt ?: 0) > 0 || resultCode < 0) {
            SmsResponse(Result.FAILURE, SmsResponseData(msgId ?: ""), message, "$resultCode")
        } else {
            SmsResponse(Result.SUCCESS, SmsResponseData(msgId ?: ""), "인증번호를 전송하였습니다.")
        }
    }
}
