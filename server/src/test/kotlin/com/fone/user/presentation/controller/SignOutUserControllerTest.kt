package com.fone.user.presentation.controller

import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPatch
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class SignOutUserControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val signOutUrl = "/api/v1/users/sign-out"

    init {
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)

        describe("#signOut") {
            context("존재하는 유저의 정보로 회원탈퇴를 하면") {
                it("성공한다.") {
                    client
                        .doPatch(signOutUrl, null, accessToken)
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
