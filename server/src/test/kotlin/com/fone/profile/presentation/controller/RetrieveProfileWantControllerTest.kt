package com.fone.profile.presentation.controller

import com.fone.common.CommonProfileCallApi
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doGet
import com.fone.common.doPost
import com.fone.common.response.CommonResponse
import com.fone.profile.presentation.dto.RetrieveProfileWantDto
import io.kotest.matchers.shouldBe
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@IntegrationTest
class RetrieveProfileWantControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val retrieveWantsUrl = "/api/v1/profiles/wants"
    private val wantUrl = "/api/v1/profiles"

    init {
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)
        val profileId = CommonProfileCallApi.register(client, accessToken)

        client
            .doPost("$wantUrl/$profileId/want", null, accessToken)
            .expectStatus()
            .isOk
            .expectBody()
            .consumeWith { println(it) }
            .jsonPath("$.result")
            .isEqualTo("SUCCESS")

        describe("#retrieve profile wants") {
            context("프로필 찜하기를 조회하면") {
                it("성공한다") {
                    client
                        .doGet(retrieveWantsUrl, accessToken, mapOf("type" to "ACTOR"))
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
            context("프로필 찜하기 타입 지정 없이 조회하면") {
                it("모든 타입 조회한다") {
                    client
                        .doGet(retrieveWantsUrl, accessToken)
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }

            context("찜 목록에 ACTOR 1, STAFF 0") {
                it("ACTOR 조회하면 찜이 1개.") {
                    val response = client
                        .doGet(retrieveWantsUrl, accessToken, mapOf("type" to "ACTOR"))
                        .expectStatus()
                        .isOk
                        .expectBody<CommonResponse<RetrieveProfileWantDto.RetrieveProfileWantResponse>>()
                        .returnResult()
                        .responseBody
                    response!!.data!!.profiles.totalElements shouldBe 1
                }
                it("Staff 조회하면 찜이 없다.") {
                    val response = client
                        .doGet(retrieveWantsUrl, accessToken, mapOf("type" to "STAFF"))
                        .expectStatus()
                        .isOk
                        .expectBody<CommonResponse<RetrieveProfileWantDto.RetrieveProfileWantResponse>>()
                        .returnResult()
                        .responseBody
                    response!!.data!!.profiles.totalElements shouldBe 0
                }
            }
        }
    }
}
