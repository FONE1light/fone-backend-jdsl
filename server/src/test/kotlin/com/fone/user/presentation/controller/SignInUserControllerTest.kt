package com.fone.user.presentation.controller

import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPost
import com.fone.user.domain.enum.LoginType
import com.fone.user.presentation.dto.SignInUserDto.SignInUserRequest
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class SignInUserControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val signInBaseUrl = "/api/v1/users/sign-in"

    init {
        val (_, email) = CommonUserCallApi.signUp(client)

        val signInUserSuccessRequest =
            SignInUserRequest(
                LoginType.APPLE,
                email,
                "test"
            )

        val signInUserFailRequest =
            SignInUserRequest(
                LoginType.APPLE,
                "test7@test.com",
                "test"
            )

        describe("#signIn") {
            context("존재하는 유저의 정보가 입력되면") {
                it("로그인에 성공한다.") {
                    client
                        .doPost(signInBaseUrl, signInUserSuccessRequest)
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
            context("존재하지 않는 유저의 정보가 입력되면") {
                it("로그인에 실패한다.") {
                    client
                        .doPost(signInBaseUrl, signInUserFailRequest)
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
