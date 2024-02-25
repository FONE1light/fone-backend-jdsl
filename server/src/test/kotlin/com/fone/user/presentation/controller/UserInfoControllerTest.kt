package com.fone.user.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPost
import com.fone.common.response.CommonResponse
import com.fone.user.domain.enum.LoginType
import com.fone.user.domain.repository.generateRandomCode
import com.fone.user.presentation.dto.SMSRequest
import com.fone.user.presentation.dto.SMSValidationRequest
import com.fone.user.presentation.dto.UserInfoSMSValidationResponse
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class UserInfoControllerTest(client: WebTestClient, private val objectMapper: ObjectMapper) :
    CustomDescribeSpec() {

    private val baseUrl = "/api/v1/users/sms"

    init {
        describe("#UserInfo 조회") {
            context("SMS 인증") {
                mockkStatic(::generateRandomCode)
                every { generateRandomCode() } returns "123456"
                it("성공하면 유저 로그인 정보 조회 가능하다") {
                    client
                        .doPost("$baseUrl/send", SMSRequest(signUpUserRequest.phoneNumber))
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                    client
                        .doPost(
                            "$baseUrl/find-id",
                            SMSValidationRequest(signUpUserRequest.phoneNumber, "123456")
                        )
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith {
                            val response: CommonResponse<UserInfoSMSValidationResponse> =
                                objectMapper.readValue(it.responseBody!!)
                            response.data!!.loginType shouldBe LoginType.PASSWORD
                        }
                }
                unmockkStatic(::generateRandomCode)
            }
        }
    }
}
