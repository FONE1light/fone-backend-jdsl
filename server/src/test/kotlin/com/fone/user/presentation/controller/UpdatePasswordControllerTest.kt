package com.fone.user.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPost
import com.fone.common.response.CommonResponse
import com.fone.user.presentation.dto.PasswordPhoneDto
import com.fone.user.presentation.dto.PasswordUpdateDto
import com.fone.user.presentation.dto.SignInUserDto
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class UpdatePasswordControllerTest(client: WebTestClient, private val objectMapper: ObjectMapper) :
    CustomDescribeSpec() {

    private val baseUrl = "/api/v1/users/password"

    init {
        var token = ""
        describe("#Password 변경") {
            context("SMS 인증") {
                val invalidTokenRequest = PasswordUpdateDto.PasswordUpdateRequest(
                    signUpUserRequest.email,
                    "newpassword1!",
                    "foo"
                )
                it("안한 상태에서는 실패하고 관련 안내 준다") {
                    client
                        .doPost("$baseUrl/update", invalidTokenRequest)
                        .expectStatus()
                        .isBadRequest
                        .expectBody()
                        .consumeWith {
                            String(it.responseBody!!) shouldContain "현재 비밀번호 업데이트 상태가 아님"
                        }
                }
                it("SMS 인증 요청 보내고 인증 코드 입력 가능하다") {
                    client
                        .doPost("$baseUrl/sms", PasswordPhoneDto.PasswordPhoneSMSRequest(signUpUserRequest.email))
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                    client
                        .doPost(
                            "$baseUrl/sms-validate",
                            PasswordPhoneDto.PasswordPhoneValidationRequest(signUpUserRequest.email, "123456")
                        )
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith {
                            val response: CommonResponse<PasswordPhoneDto.PasswordPhoneValidationResponse> =
                                objectMapper.readValue(it.responseBody!!)
                            response.data!!.response shouldBe PasswordPhoneDto.ResponseType.SUCCESS
                            token = response.data!!.token!!
                        }
                }
                it("한 상태에서는 토큰 유효성 검토한다.") {
                    client
                        .doPost("$baseUrl/update", invalidTokenRequest)
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith {
                            String(it.responseBody!!) shouldContain "INVALID_TOKEN"
                        }
                }
                it("유효한 토큰 보낼 경우 성공한다.") {
                    val invalidTokenRequest = PasswordUpdateDto.PasswordUpdateRequest(
                        signUpUserRequest.email,
                        "newpassword1!",
                        token
                    )
                    client
                        .doPost("$baseUrl/update", invalidTokenRequest)
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith {
                            val response: CommonResponse<PasswordUpdateDto.PasswordUpdateResponse> =
                                objectMapper.readValue(it.responseBody!!)
                            response.data?.response shouldBe PasswordUpdateDto.ResponseType.SUCCESS
                        }
                    client
                        .doPost(
                            "$baseUrl/sign-in",
                            SignInUserDto.PasswordSignInUserRequest(signUpUserRequest.email, "newpassword1!")
                        )
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
