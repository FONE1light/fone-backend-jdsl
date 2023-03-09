package com.fone.profile.presentation.controller

import com.fone.common.CommonProfileCallApi
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doDelete
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class DeleteProfileControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val deleteUrl = "/api/v1/profiles"

    init {
        val (accessToken, email) = CommonUserCallApi.getAccessToken(client)
        val profileId = CommonProfileCallApi.register(client, accessToken)

        describe("#delete profile") {
            context("존재하는 프로필을 삭제하면") {
                it("성공한다") {
                    client.doDelete("$deleteUrl/$profileId", accessToken).expectStatus().isOk.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("SUCCESS")
                }
            }

            context("존재하지 않는 프로필을 삭제하면") {
                it("실패한다") {
                    client.doDelete("$deleteUrl/1231", accessToken).expectStatus().isOk.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
            }
        }
    }
}
