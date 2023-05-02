package com.fone.user.presentation.controller

import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doGet
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class ReissueUserControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val logOutUrl = "/api/v1/users"

    init {
        describe("#Reissue") {
            context("토큰 재발급 인증 실패시") {
                it("401 돌려준다") {
                    client
                        .doGet(logOutUrl, null)
                        .expectStatus()
                        .isUnauthorized
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("FAIL")
                }
            }
        }
    }
}
