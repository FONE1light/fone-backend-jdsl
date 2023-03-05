package com.fone.jobOpening.presentation.controller

import com.fone.common.CommonJobOpeningCallApi
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doGet
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class RetrieveJobOpeningControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val retrieveUrl = "/api/v1/job-openings"

    init {
        val (accessToken, email) = CommonUserCallApi.getAccessToken(client)
        val jobOpeningId = CommonJobOpeningCallApi.register(client, accessToken)

        describe("#retrieve jobOpenings") {
            context("구인구직 리스트를 조회하면") {
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

        describe("#retrieve jobOpening") {
            context("존재하는 구인구직을 상세 조회하면") {
                it("성공한다") {
                    client
                        .doGet("$retrieveUrl/$jobOpeningId", accessToken, mapOf("type" to "ACTOR"))
                        .expectStatus().isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }

            context("존재하지 않는 구인구직을 상세 조회하면") {
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
