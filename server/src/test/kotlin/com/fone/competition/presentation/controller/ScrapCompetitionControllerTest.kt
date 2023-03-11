package com.fone.competition.presentation.controller

import com.fone.common.CommonCompetitionCallApi
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPost
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class ScrapCompetitionControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val scrapUrl = "/api/v1/competitions"

    init {
        val (accessToken, email) = CommonUserCallApi.getAccessToken(client)
        val competitionId = CommonCompetitionCallApi.register(client, accessToken)

        describe("#scrap competition") {
            context("존재하는 공모전을 스크랩 하면") {
                it("성공한다") {
                    client
                        .doPost("$scrapUrl/$competitionId/scrap", null, accessToken)
                        .expectStatus().isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
            context("존재하지 않는 공모전을 스크랩 하면") {
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
