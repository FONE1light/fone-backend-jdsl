package com.fone.user.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.TestGenerator
import com.fone.common.doPost
import com.fone.common.entity.CategoryType
import com.fone.common.entity.Gender
import com.fone.common.response.CommonResponse
import com.fone.user.domain.enum.Job
import com.fone.user.presentation.dto.PasswordValidationDto
import com.fone.user.presentation.dto.PasswordValidationDto.PasswordValidationRequest
import com.fone.user.presentation.dto.PasswordValidationDto.PasswordValidationResponse
import com.fone.user.presentation.dto.SignInUserDto
import com.fone.user.presentation.dto.SignUpUserDto
import io.kotest.matchers.shouldBe
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate

@IntegrationTest
class EmailUserAuthenticationTest(
    client: WebTestClient,
    private val objectMapper: ObjectMapper,
) : CustomDescribeSpec() {
    private val signupUrl = "/api/v1/users/email/sign-up"
    private val signinUrl = "/api/v1/users/email/sign-in"
    private val validationUrl = "/api/v1/users/password/validate"

    init {
        val weakPasswordSignUpRequest =
            SignUpUserDto.EmailSignUpUserRequest(
                Job.ACTOR,
                listOf(CategoryType.ETC),
                "name_test_password",
                "test_password",
                LocalDate.now(),
                Gender.IRRELEVANT,
                null,
                TestGenerator.getRandomPhoneNumber(),
                "test_password@test.com",
                "test_password@test.com",
                true,
                true,
                true,
                "password1",
                ""
            )
        val signInRequest = SignInUserDto.EmailSignInUserRequest(
            "test_password@test.com",
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
                        .isBadRequest
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("FAIL")
                }
            }
        }
        context("#Password 중복 조회") {
            it("이미 존재하는 email인 경우 Invalid 돌려준다") {
                client
                    .doPost(
                        validationUrl,
                        PasswordValidationRequest(signInRequest.email, signInRequest.password)
                    )
                    .expectStatus()
                    .isOk
                    .expectBody()
                    .consumeWith {
                        val response =
                            objectMapper.readValue<CommonResponse<PasswordValidationResponse>>(it.responseBody!!)
                        response.data!!.response shouldBe PasswordValidationDto.ResponseType.INVALID
                    }
                    .jsonPath("$.result")
                    .isEqualTo("SUCCESS")
            }
            it("존재하지 않은 email인 경우 Valid 돌려준다") {
                client
                    .doPost(
                        validationUrl,
                        PasswordValidationRequest("abcdefg@gmail.com", signInRequest.password)
                    )
                    .expectStatus()
                    .isOk
                    .expectBody()
                    .consumeWith {
                        val response =
                            objectMapper.readValue<CommonResponse<PasswordValidationResponse>>(it.responseBody!!)
                        response.data!!.response shouldBe PasswordValidationDto.ResponseType.VALID
                    }
                    .jsonPath("$.result")
                    .isEqualTo("SUCCESS")
            }
        }
    }
}

val signUpUserRequest =
    SignUpUserDto.EmailSignUpUserRequest(
        Job.ACTOR,
        listOf(CategoryType.ETC),
        "name_test_password",
        "test_password",
        LocalDate.now(),
        Gender.IRRELEVANT,
        null,
        TestGenerator.getRandomPhoneNumber(),
        "test_password@test.com",
        "test_password@test.com",
        true,
        true,
        true,
        "Somepassword1!",
        ""
    )
