package com.fone.jobOpening.presentation.controller

import com.fone.common.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class DeleteJobOpeningControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val deleteUrl = "/api/v1/job-openings"

    init {
        val (accessToken, email) = CommonUserCallApi.getAccessToken(client)
        val jobOpeningId = CommonJobOpeningCallApi.register(client, accessToken)

        describe("#delete jobOpening") {
            context("존재하는 구인구직을 삭제하면") {
                it("성공한다") {
                    client
                        .doDelete("$deleteUrl/$jobOpeningId", accessToken)
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }

            context("존재하지 않는 구인구직을 삭제하면") {
                it("성공한다") {
                    client
                        .doDelete("$deleteUrl/1231", accessToken)
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("FAIL")
                }
            }
        }
    }
}
