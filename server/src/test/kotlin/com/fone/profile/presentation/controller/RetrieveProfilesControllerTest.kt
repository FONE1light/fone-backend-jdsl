package com.fone.profile.presentation.controller

import com.fone.common.CommonProfileCallApi
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doGet
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class RetrieveProfilesControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val retrieveUrl = "/api/v1/profiles"

    init {
        val (accessToken, email) = CommonUserCallApi.getAccessToken(client)
        val profileId = CommonProfileCallApi.register(client, accessToken)

        describe("#retrieve profiles") {
            context("프로필 리스트를 조회하면") {
                it("성공한다") {
                    client
                        .doGet(retrieveUrl, accessToken, mapOf("type" to "ACTOR"))
                        .expectStatus().isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
        }

        describe("#retrieve profile") {
            context("존재하는 프로필을 상세 조회하면") {
                it("성공한다") {
                    client
                        .doGet("$retrieveUrl/$profileId", accessToken, mapOf("type" to "ACTOR"))
                        .expectStatus().isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }

            context("존재하지 않는 프로필을 상세 조회하면") {
                it("실패한다") {
                    client
                        .doGet("$retrieveUrl/1231", accessToken, mapOf("type" to "ACTOR"))
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
