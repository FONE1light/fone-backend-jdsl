package com.fone.user.presentation.controller

import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.TestGenerator
import com.fone.common.doPost
import com.fone.common.entity.CategoryType
import com.fone.common.entity.Gender
import com.fone.user.domain.enum.Job
import com.fone.user.domain.enum.LoginType
import com.fone.user.presentation.dto.SignInUserDto
import com.fone.user.presentation.dto.SignUpUserDto
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate

@IntegrationTest
class PasswordUserAuthenticationTest(client: WebTestClient) : CustomDescribeSpec() {
    private val signupUrl = "/api/v1/users/sign-up"
    private val signinUrl = "/api/v1/users/sign-in"

    init {
        val weakPasswordSignUpRequest =
            SignUpUserDto.SignUpUserRequest(
                Job.ACTOR,
                listOf(CategoryType.ETC),
                "test_password",
                LocalDate.now(),
                Gender.IRRELEVANT,
                null,
                TestGenerator.getRandomPhoneNumber(),
                "test_password@test.com",
                "test_password@test.com",
                LoginType.PASSWORD,
                true,
                true,
                true,
                null,
                "password1"
            )
        val signUpUserRequest =
            SignUpUserDto.SignUpUserRequest(
                Job.ACTOR,
                listOf(CategoryType.ETC),
                "test_password",
                LocalDate.now(),
                Gender.IRRELEVANT,
                null,
                TestGenerator.getRandomPhoneNumber(),
                "test_password@test.com",
                "test_password@test.com",
                LoginType.PASSWORD,
                true,
                true,
                true,
                null,
                "Somepassword1!"
            )
        val signInRequest = SignInUserDto.SignInUserRequest(
            LoginType.PASSWORD,
            "test_password@test.com",
            null,
            "Somepassword1!"
        )
        describe("#Password SignUp") {
            context("약한 Password을 이용하면") {
                it("유저 생성 실패한다") {
                    client
                        .doPost(signupUrl, weakPasswordSignUpRequest)
                        .expectStatus()
                        .isBadRequest
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("FAIL")
                }
            }
            context("유효한 Password을 이용해서") {
                it("새 유저를 생성한다") {
                    client
                        .doPost(signupUrl, signUpUserRequest)
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
            context("이미 존재하는 유저라면") {
                it("로그인 성공한다.") {
                    client
                        .doPost(signinUrl, signInRequest)
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
                it("유저 생성을 실패한다.") {
                    client
                        .doPost(signupUrl, signUpUserRequest)
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
