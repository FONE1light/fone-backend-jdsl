package com.fone.sms.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fone.sms.data.AligoSmsRequest
import com.fone.sms.data.AligoSmsResponse
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
class AligoService(private val objectMapper: ObjectMapper) {
    private val webClient = WebClient.builder().baseUrl("https://apis.aligo.in/send/")
        .defaultHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .build()

    suspend fun sendToAligo(request: AligoSmsRequest): AligoSmsResponse {
        val responseBody = webClient.post()
            .body(BodyInserters.fromFormData(request.toMap()))
            .retrieve()
            .awaitBody<String>()
        return objectMapper.readValue(responseBody)
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
