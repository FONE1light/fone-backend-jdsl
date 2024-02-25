package com.fone.user.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPatch
import com.fone.common.doPost
import com.fone.common.response.CommonResponse
import com.fone.user.domain.repository.generateRandomCode
import com.fone.user.presentation.dto.EmailSignInUserRequest
import com.fone.user.presentation.dto.PasswordSMSValidationResponse
import com.fone.user.presentation.dto.PasswordUpdateRequest
import com.fone.user.presentation.dto.SMSRequest
import com.fone.user.presentation.dto.SMSValidationRequest
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class UpdatePasswordControllerTest(client: WebTestClient, private val objectMapper: ObjectMapper) :
    CustomDescribeSpec() {

    private val baseUrl = "/api/v1/users"

    init {
        describe("#Password 변경") {
            mockkStatic(::generateRandomCode)
            every { generateRandomCode() } returns "123456"
            var token = ""
            context("SMS 인증") {
                val invalidTokenRequest = PasswordUpdateRequest(
                    signUpUserRequest.phoneNumber,
                    "newPassword1!",
                    "foo"
                )
                it("안한 상태에서는 실패하고 관련 안내 준다") {
                    client
                        .doPatch("$baseUrl/password", invalidTokenRequest)
                        .expectStatus()
                        .isBadRequest
                        .expectBody()
                        .consumeWith {
                            String(it.responseBody!!) shouldContain "현재 비밀번호 업데이트 상태가 아님"
                        }
                }
                it("SMS 인증 요청 보내고 인증 코드 입력 가능하다") {
                    client
                        .doPost("$baseUrl/sms/send", SMSRequest(signUpUserRequest.phoneNumber))
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                    client
                        .doPost(
                            "$baseUrl/sms/find-password",
                            SMSValidationRequest(signUpUserRequest.phoneNumber, "123456")
                        )
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith {
                            val response: CommonResponse<PasswordSMSValidationResponse> =
                                objectMapper.readValue(it.responseBody!!)
                            response.result shouldBe CommonResponse.Result.SUCCESS
                            token = response.data!!.token!!
                        }
                }
                it("한 상태에서는 토큰 유효성 검토한다.") {
                    client
                        .doPatch("$baseUrl/password", invalidTokenRequest)
                        .expectStatus()
                        .isUnauthorized
                        .expectBody()
                }
                it("유효한 토큰 보낼 경우 성공한다.") {
                    client
                        .doPatch(
                            "$baseUrl/password",
                            PasswordUpdateRequest(
                                signUpUserRequest.phoneNumber,
                                "newPassword1!",
                                token
                            )
                        )
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith {
                            val response: CommonResponse<Unit> =
                                objectMapper.readValue(it.responseBody!!)
                            response.result shouldBe CommonResponse.Result.SUCCESS
                        }
                    client
                        .doPost(
                            "$baseUrl/email/sign-in",
                            EmailSignInUserRequest(signUpUserRequest.email, "newPassword1!")
                        )
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
            unmockkStatic(::generateRandomCode)
        }
    }
}
