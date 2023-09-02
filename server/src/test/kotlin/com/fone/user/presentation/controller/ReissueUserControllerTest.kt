package com.fone.user.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doGet
import com.fone.common.doPost
import com.fone.common.jwt.Token
import com.fone.common.response.CommonResponse
import com.fone.user.presentation.dto.ReissueTokenDto
import com.fone.user.presentation.dto.SignInUserDto
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.core.ParameterizedTypeReference
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class ReissueUserControllerTest(client: WebTestClient, objectMapper: ObjectMapper) : CustomDescribeSpec() {

    private val reissueUrl = "/api/v1/users/reissue"
    private val token = objectMapper.convertValue<SignInUserDto.SignInUserResponse>(
        CommonUserCallApi.getToken(
            client,
            "test-nick",
            "some-email@something.com"
        )
    ).token
    private val retrieveMyPageUrl = "/api/v1/users"

    init {
        describe("#Reissue") {
            context("토큰 재발급 시도 시") {
                var reissued: Token? = null
                it("Access 토큰 재발행한다") {
                    Thread.sleep(1000)
                    client
                        .doPost(
                            reissueUrl,
                            ReissueTokenDto.ReissueTokenRequest(token.accessToken, token.refreshToken)
                        )
                        .expectStatus()
                        .isOk
                        .expectBody(object : ParameterizedTypeReference<CommonResponse<Token>>() {})
                        .consumeWith {
                            reissued = it.responseBody!!.data!!
                            reissued!!.refreshToken shouldBe token.refreshToken
                            reissued!!.accessToken shouldNotBe token.accessToken
                        }
                }
                it("재발행한 토큰으로 인증 성공한다") {
                    client
                        .doGet(retrieveMyPageUrl, reissued!!.accessToken)
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
