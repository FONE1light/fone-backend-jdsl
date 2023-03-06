package com.fone.jobOpening.presentation.controller

import com.fone.common.CommonJobOpeningCallApi
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPost
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class ScrapJobOpeningControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val scrapUrl = "/api/v1/job-openings"

    init {
        val (accessToken, email) = CommonUserCallApi.getAccessToken(client)
        val jobOpeningId = CommonJobOpeningCallApi.register(client, accessToken)

        describe("#scrap jobOpenings") {
            context("존재하는 구인구직을 스크랩 하면") {
                it("성공한다") {
                    client
                        .doPost("$scrapUrl/$jobOpeningId/scrap", null, accessToken)
                        .expectStatus().isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
            context("존재하지 않는 구인구직을 스크랩 하면") {
                it("실패한다") {
                    client
                        .doPost("$scrapUrl/1231/scrap", null, accessToken)
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
