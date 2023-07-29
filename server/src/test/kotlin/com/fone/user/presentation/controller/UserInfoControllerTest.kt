package com.fone.user.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPost
import com.fone.common.response.CommonResponse
import com.fone.user.domain.enum.LoginType
import com.fone.user.presentation.dto.SMSUserDto
import io.kotest.matchers.shouldBe
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class UserInfoControllerTest(client: WebTestClient, private val objectMapper: ObjectMapper) :
    CustomDescribeSpec() {

    private val baseUrl = "/api/v1/users/sms"

    init {
        describe("#UserInfo 조회") {
            context("SMS 인증") {
                it("성공하면 유저 로그인 정보 조회 가능하다") {
                    client
                        .doPost(baseUrl, SMSUserDto.SMSRequest(signUpUserRequest.phoneNumber))
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                    client
                        .doPost(
                            "$baseUrl/user",
                            SMSUserDto.SMSValidationRequest(signUpUserRequest.phoneNumber, "123456")
                        )
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith {
                            val response: CommonResponse<SMSUserDto.UserInfoSMSValidationResponse> =
                                objectMapper.readValue(it.responseBody!!)
                            response.data!!.response shouldBe SMSUserDto.ResponseType.SUCCESS
                            response.data!!.loginType shouldBe LoginType.PASSWORD
                        }
                }
            }
        }
    }
}
