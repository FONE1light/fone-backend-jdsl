package com.fone.sms

import com.fone.sms.presentation.controllers.SmsController
import com.fone.sms.presentation.data.SMSVerificationRequest
import com.fone.sms.presentation.data.SmsVerificationResponse
import io.kotest.core.spec.style.FreeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@SpringBootTest
class SmsApplicationTests(controller: SmsController) : FreeSpec() {
    override fun extensions() = listOf(SpringExtension)
    private val webClient = WebTestClient.bindToController(controller).build()

    init {
        "SMS Test" - {
            "Should Be able to send SmsRequest".config(enabled = false) {
                webClient.post()
                    .uri("/sms/send-sms")
                    .bodyValue(SMSVerificationRequest("01000000000", "123")) // 전화번호 입력
                    .exchange()
                    .expectStatus().isOk
                    .expectBody<SmsVerificationResponse>()
                    .consumeWith { it.responseBody!!.message shouldBe "인증번호를 전송하였습니다." }
            }
        }
    }
}
