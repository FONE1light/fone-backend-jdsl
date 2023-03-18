package com.fone.profile.presentation.controller

import com.fone.common.CommonProfileCallApi
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPost
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class WantProfileControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val wantUrl = "/api/v1/profiles"

    init {
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)
        val profileId = CommonProfileCallApi.register(client, accessToken)

        describe("#want profile") {
            context("존재하는 프로필을 스크랩 하면") {
                it("성공한다") {
                    client
                        .doPost("$wantUrl/$profileId/want", null, accessToken)
                        .expectStatus().isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
            context("존재하지 않는 프로필을 스크랩 하면") {
                it("실패한다") {
                    client
                        .doPost("$wantUrl/1231/want", null, accessToken)
                        .expectStatus().isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("FAIL")
                }
            }
        }
    }
}
