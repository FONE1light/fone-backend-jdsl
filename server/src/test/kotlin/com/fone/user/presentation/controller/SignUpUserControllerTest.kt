package com.fone.user.presentation.controller

import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPost
import com.fone.common.entity.CategoryType
import com.fone.common.entity.Gender
import com.fone.user.domain.enum.Job
import com.fone.user.domain.enum.LoginType
import com.fone.user.presentation.dto.SignUpUserDto
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate

@IntegrationTest
class SignUpUserControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val baseUrl = "/api/v1/users/social/sign-up"

    init {
        val signUpUserRequest =
            SignUpUserDto.SocialSignUpUserRequest(
                Job.ACTOR,
                listOf(CategoryType.ETC),
                "testName5",
                LocalDate.now(),
                Gender.IRRELEVANT,
                null,
                "010-1234-1234",
                "test5@test.com",
                "test5@test.com",
                LoginType.APPLE,
                true,
                true,
                true,
                "test"
            )

        describe("#signUp") {
            context("새 유저 정보가 들어오면") {
                it("새 유저를 생성한다") {
                    client
                        .doPost(baseUrl, signUpUserRequest)
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }

            context("이미 존재하는 유저라면") {
                it("유저 생성을 실패한다.") {
                    client
                        .doPost(baseUrl, signUpUserRequest)
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
