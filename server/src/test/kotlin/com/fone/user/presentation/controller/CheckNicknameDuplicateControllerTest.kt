package com.fone.user.presentation.controller

import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doGet
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class CheckNicknameDuplicateControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val checkNicknameDuplicateUrl = "/api/v1/users/check-nickname-duplication"

    init {
        val (nickname, _) = CommonUserCallApi.signUp(client)

        describe("#checkNicknameDuplicate") {
            context("존재하지 않는 닉네임을 입력하면") {
                it("성공한다.") {
                    client.doGet(checkNicknameDuplicateUrl, null, mapOf("nickname" to "123"))
                        .expectStatus().isOk.expectBody().consumeWith { println(it) }.jsonPath("$.data.isDuplicate")
                        .isEqualTo(false)
                }
            }
            context("존재하는 닉네임을 입력하면") {
                it("실패한다.") {
                    client.doGet(checkNicknameDuplicateUrl, null, mapOf("nickname" to nickname))
                        .expectStatus().isOk.expectBody().consumeWith { println(it) }.jsonPath("$.data.isDuplicate")
                        .isEqualTo(true)
                }
            }
        }
    }
}
