package com.fone.competition.presentation.controller

import com.fone.common.CommonCompetitionCallApi
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doGet
import com.fone.common.doPost
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class RetrieveCompetitionScrapControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val retrieveScrapsUrl = "/api/v1/competitions/scraps"
    private val scrapUrl = "/api/v1/competitions"

    init {
        val (accessToken, email) = CommonUserCallApi.getAccessToken(client)
        val competitionId = CommonCompetitionCallApi.register(client, accessToken)

        client.doPost("$scrapUrl/$competitionId/scrap", null, accessToken).expectStatus().isOk.expectBody()
            .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("SUCCESS")

        describe("#retrieve competition scraps") {
            context("공모전 스크랩을 조회하면") {
                it("성공한다") {
                    client.doGet(retrieveScrapsUrl, accessToken)
                        .expectStatus().isOk.expectBody().consumeWith { println(it) }.jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
        }
    }
}
