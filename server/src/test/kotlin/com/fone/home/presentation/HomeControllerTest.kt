package com.fone.home.presentation

import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doGet
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class HomeControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val retrieveUrl = "/api/v1/homes"

    init {
        beforeSpec {

        }

        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)

        describe("#retrieveHome") {
            context("when retrieve home") {
                it("should return home info") {
                    client.doGet(retrieveUrl, accessToken).expectStatus().isOk.expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
        }
    }
}
