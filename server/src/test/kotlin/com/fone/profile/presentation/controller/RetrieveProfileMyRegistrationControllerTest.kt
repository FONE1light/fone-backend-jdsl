package com.fone.profile.presentation.controller

import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doGet
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class RetrieveProfileMyRegistrationControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val retrieveMyRegistrationUrl = "/api/v1/profiles/my-registrations"

    init {
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)

        describe("#retrieve profile my registration") {
            context("내가 등록한 프로필을 조회하면") {
                it("성공한다") {
                    client
                        .doGet(retrieveMyRegistrationUrl, accessToken)
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
