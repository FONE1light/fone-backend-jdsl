package com.fone.jobOpening.presentation.controller

import com.fone.common.CommonJobOpeningCallApi
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doGet
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class RetrieveMySimilarJobOpeningControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val retrieveMySimilarUrl = "/api/v1/job-openings/my-similar"

    init {
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)
        CommonJobOpeningCallApi.register(client, accessToken)

        describe("#retrieve my similar jobOpenings") {
            context("나와 비슷한 사람들이 보고 있는 구인구직 리스트를 조회하면") {
                it("성공한다") {
                    client
                        .doGet(retrieveMySimilarUrl, accessToken, mapOf("type" to "ACTOR"))
                        .expectStatus().isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
        }
    }
}
