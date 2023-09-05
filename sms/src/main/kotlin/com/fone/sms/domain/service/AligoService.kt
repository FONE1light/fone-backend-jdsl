package com.fone.sms.domain.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fone.common.exception.SMSBackendException
import com.fone.sms.domain.dto.AligoSmsRequest
import com.fone.sms.domain.dto.AligoSmsResponse
import com.fone.sms.domain.dto.toMap
import com.fone.sms.presentation.dto.SMSSendDTO.SMSSendRequest
import com.fone.sms.presentation.dto.SMSSendDTO.SMSSendResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
class AligoService(
    @Value("\${credentials.sms.key}") private val key: String,
    @Value("\${credentials.sms.user-id}") private val userId: String,
    @Value("\${credentials.sms.sender}") private val sender: String,
    private val objectMapper: ObjectMapper,
) {
    private val webClient = WebClient.builder().baseUrl("https://apis.aligo.in/send/")
        .defaultHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .build()

    suspend fun sendToAligo(request: SMSSendRequest): SMSSendResponse {
        val aligoRequest = AligoSmsRequest(receiver = request.phone, msg = "인증번호는 ${request.code} 입니다")
        if (aligoRequest.key == "") aligoRequest.initCredentials()
        val response = sendToAligo(aligoRequest)
        response.validation()
        return SMSSendResponse(response.msgId ?: "")
    }

    suspend fun sendToAligo(aligoRequest: AligoSmsRequest): AligoSmsResponse {
        val responseBody = webClient.post()
            .body(BodyInserters.fromFormData(aligoRequest.toMap()))
            .retrieve()
            .awaitBody<String>()
        return objectMapper.readValue(responseBody)
    }
    private fun AligoSmsRequest.initCredentials() {
        key = this@AligoService.key
        userId = this@AligoService.userId
        sender = this@AligoService.sender
    }

    private fun AligoSmsResponse.validation() {
        if ((this.errorCnt ?: 0) > 0 || resultCode < 0) {
            throw SMSBackendException()
        }
    }
}
