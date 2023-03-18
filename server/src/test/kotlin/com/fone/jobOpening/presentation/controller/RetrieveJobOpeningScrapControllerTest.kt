package com.fone.jobOpening.presentation.controller

import com.fone.common.CommonJobOpeningCallApi
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doGet
import com.fone.common.doPost
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class RetrieveJobOpeningScrapControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val retrieveScrapsUrl = "/api/v1/job-openings/scraps"
    private val scrapUrl = "/api/v1/job-openings"

    init {
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)
        val jobOpeningId = CommonJobOpeningCallApi.register(client, accessToken)

        client.doPost("$scrapUrl/$jobOpeningId/scrap", null, accessToken).expectStatus().isOk.expectBody()
            .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("SUCCESS")

        describe("#retrieve jobOpening scraps") {
            context("구인구직 스크랩을 조회하면") {
                it("성공한다") {
                    client.doGet(retrieveScrapsUrl, accessToken, mapOf("type" to "ACTOR"))
                        .expectStatus().isOk.expectBody().consumeWith { println(it) }.jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
        }
    }
}
