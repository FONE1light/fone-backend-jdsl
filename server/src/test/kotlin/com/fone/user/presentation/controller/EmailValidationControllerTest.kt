package com.fone.user.presentation.controller

import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPost
import com.fone.user.presentation.dto.EmailValidationDto
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class EmailValidationControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val baseUrl = "/api/v1/users/email"

    init {
        val sendRequest = EmailValidationDto.EmailSendRequest("abc@abc.com")
        val validationRequest = EmailValidationDto.EmailValidationRequest("abc@abc.com", "")

        describe("#Email Validation") {
            context("이메일 전송 요청 보내면") {
                it("성공한다") {
                    client
                        .doPost("$baseUrl/send", sendRequest)
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }

            context("이메일 코드 인증 성공하면") {
                it("토큰 돌려준다") {
                    client
                        .doPost("$baseUrl/validate", validationRequest)
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
        }
    }
}
