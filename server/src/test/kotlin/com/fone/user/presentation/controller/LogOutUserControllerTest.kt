package com.fone.user.presentation.controller

import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPost
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class LogOutUserControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val logOutUrl = "/api/v1/users/log-out"

    init {
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)

        describe("#logOut") {
            context("등록된 유저가 로그아웃을 하면") {
                it("성공한다") {
                    client
                        .doPost(logOutUrl, null, accessToken)
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
