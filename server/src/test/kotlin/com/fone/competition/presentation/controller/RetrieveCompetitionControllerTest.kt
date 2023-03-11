package com.fone.competition.presentation.controller

import com.fone.common.CommonCompetitionCallApi
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doGet
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class RetrieveCompetitionControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val retrieveUrl = "/api/v1/competitions"

    init {
        val (accessToken, email) = CommonUserCallApi.getAccessToken(client)
        val competitionId = CommonCompetitionCallApi.register(client, accessToken)

        describe("#retrieve competitions") {
            context("공모전 리스트를 조회하면") {
                it("성공한다") {
                    client.doGet(retrieveUrl, accessToken).expectStatus().isOk.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("SUCCESS")
                }
            }
        }

        describe("#retrieve competition") {
            context("존재하는 공모전을 상세 조회하면") {
                it("성공한다") {
                    client.doGet("$retrieveUrl/$competitionId", accessToken)
                        .expectStatus().isOk.expectBody().consumeWith { println(it) }.jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }

            context("존재하지 않는 공모전을 상세 조회하면") {
                it("실패한다") {
                    client.doGet("$retrieveUrl/1231", accessToken)
                        .expectStatus().isOk.expectBody().consumeWith { println(it) }.jsonPath("$.result")
                        .isEqualTo("FAIL")
                }
            }
        }
    }
}
