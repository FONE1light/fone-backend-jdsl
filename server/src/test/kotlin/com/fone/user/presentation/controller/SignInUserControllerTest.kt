package com.fone.user.presentation.controller

import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPost
import com.fone.common.entity.CategoryType
import com.fone.common.entity.Gender
import com.fone.user.domain.enum.Job
import com.fone.user.domain.enum.SocialLoginType
import com.fone.user.presentation.dto.SignInUserDto.SignInUserRequest
import com.fone.user.presentation.dto.SignUpUserDto
import java.time.LocalDate
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class SignInUserControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val signInBaseUrl = "/api/v1/users/sign-in"
    private val signUpBaseUrl = "/api/v1/users/sign-up"

    init {
        val (signInUserSuccessRequest, signInUserFailRequest) = given(client)

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

    private fun given(client: WebTestClient): Pair<SignInUserRequest, SignInUserRequest> {
        val signUpUserRequest =
            SignUpUserDto.SignUpUserRequest(
                Job.ACTOR,
                listOf(CategoryType.ETC),
                "test6",
                LocalDate.now(),
                Gender.IRRELEVANT,
                null,
                "010-1234-1234",
                "test6@test.com",
                SocialLoginType.APPLE,
                true,
                true,
                true,
                "test",
            )

        val signInUserSuccessRequest =
            SignInUserRequest(
                SocialLoginType.APPLE,
                "test6@test.com",
                "test",
            )

        val signInUserFailRequest =
            SignInUserRequest(
                SocialLoginType.APPLE,
                "test7@test.com",
                "test",
            )

        client
            .doPost(signUpBaseUrl, signUpUserRequest)
            .expectStatus()
            .isOk
            .expectBody()
            .consumeWith { println(it) }
            .jsonPath("$.result")
            .isEqualTo("SUCCESS")
        return Pair(signInUserSuccessRequest, signInUserFailRequest)
    }
}
