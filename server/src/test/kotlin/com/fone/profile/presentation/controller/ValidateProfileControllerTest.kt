package com.fone.profile.presentation.controller

import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPost
import com.fone.common.entity.Gender
import com.fone.profile.domain.enum.SNS
import com.fone.profile.presentation.dto.ValidateProfileDto
import com.fone.profile.presentation.dto.common.ProfileSnsUrl
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate

@IntegrationTest
class ValidateProfileControllerTest(client: WebTestClient) : CustomDescribeSpec() {
    init {
        val url = "/api/v1/profiles/validate"
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)
        describe("#Profile 검증 API") {
            context("basic 페이지") {
                it("성공한다") {
                    val request =
                        ValidateProfileDto.BasicPageValidation(
                            "제목",
                            "후킹멘트",
                            listOf("https://s3-ap-northeast-2.amazonaws.com/f-one-image/prod/user-profile/image.jpg")
                        )
                    client.doPost("$url/basic", request, accessToken)
                        .expectStatus().isOk.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("SUCCESS")
                }
                it("실패한다") {
                    val request =
                        ValidateProfileDto.BasicPageValidation(
                            "제목",
                            "후킹멘트",
                            listOf("")
                        )
                    client.doPost("$url/basic", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
            }
            context("details 페이지") {
                it("성공한다") {
                    val request =
                        ValidateProfileDto.DetailPageValidation(
                            LocalDate.now(),
                            Gender.MAN,
                            170,
                            300,
                            "mail@mail.com",
                            listOf(),
                            "고추장 잘 먹음",
                            listOf(ProfileSnsUrl("123", SNS.YOUTUBE)),
                        )
                    client.doPost("$url/details", request, accessToken)
                        .expectStatus().isOk.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("SUCCESS")
                }
                it("실패한다") {
                    val request =
                        ValidateProfileDto.DetailPageValidation(
                            LocalDate.now(),
                            Gender.MAN,
                            null,
                            300,
                            "mail@mail.com",
                            listOf(),
                            "고추장 잘 먹음",
                            listOf(ProfileSnsUrl("123", SNS.YOUTUBE)),
                        )
                    client.doPost("$url/details", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
            }
        }
    }
}
