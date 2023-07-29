package com.fone.user.infrastructure

import com.fone.user.domain.repository.SMSRepository
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.random.Random

@Service
class SMSRepositoryImpl(@Value("\${security.sms.url}") smsUrl: String) : SMSRepository {
    private val webClient = WebClient.create(smsUrl)
    private val rng = Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
    override suspend fun sendValidationMessage(phone: String, code: String) {
        val responseMono: Mono<Map<String, Any>> = webClient.post()
            .bodyValue(SMSRepository.SMSRequest(phone, code))
            .retrieve()
            .bodyToMono()
        val response = responseMono.awaitSingle()
        assert(response["result"] == "SUCCESS")
    }

    override fun generateRandomCode(): String {
        return String.format("%05d", rng.nextInt(0, 1000000))
    }
}
