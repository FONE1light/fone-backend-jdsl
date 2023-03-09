package com.fone.profile.presentation.controller

import com.fone.common.CommonProfileCallApi
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doGet
import com.fone.common.doPost
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class RetrieveProfileWantControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val retrieveWantsUrl = "/api/v1/profiles/wants"
    private val wantUrl = "/api/v1/profiles"

    init {
        val (accessToken, email) = CommonUserCallApi.getAccessToken(client)
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
        }
    }
}
