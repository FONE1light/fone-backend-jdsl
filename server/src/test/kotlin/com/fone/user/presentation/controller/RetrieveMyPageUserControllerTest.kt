package com.fone.user.presentation.controller

import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doGet
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class RetrieveMyPageUserControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val retrieveMyPageUrl = "/api/v1/users"
    init {
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)

        describe("#retrieveMyPage") {
            context("존재하는 유저의 정보로 마이페이지를 조회하면") {
                it("성공한다") {
                    client
                        .doGet(retrieveMyPageUrl, accessToken)
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
        }
    }
}
