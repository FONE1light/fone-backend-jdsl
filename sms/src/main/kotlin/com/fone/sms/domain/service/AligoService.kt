package com.fone.sms.domain.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fone.sms.domain.data.AligoSmsRequest
import com.fone.sms.domain.data.AligoSmsResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
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

    suspend fun sendToAligo(request: AligoSmsRequest): AligoSmsResponse {
        if (request.key == "") request.initCredentials()
        val responseBody = webClient.post()
            .body(BodyInserters.fromFormData(request.toMap()))
            .retrieve()
            .awaitBody<String>()
        return objectMapper.readValue(responseBody)
    }
    private fun AligoSmsRequest.initCredentials() {
        key = this@AligoService.key
        userId = this@AligoService.userId
        sender = this@AligoService.sender
    }

    private fun AligoSmsRequest.toMap(): MultiValueMap<String, String> {
        val map = LinkedMultiValueMap<String, String>()
        map.add("key", key)
        map.add("user_id", userId)
        map.add("sender", sender)
        map.add("receiver", receiver)
        map.add("msg", msg)
        map.add("msg_type", msgType)
        return map
    }
}
