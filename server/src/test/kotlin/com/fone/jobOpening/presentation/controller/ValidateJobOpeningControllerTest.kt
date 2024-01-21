package com.fone.jobOpening.presentation.controller

import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPost
import com.fone.common.entity.Career
import com.fone.common.entity.CategoryType
import com.fone.common.entity.Gender
import com.fone.jobOpening.presentation.dto.ValidateJobOpeningDto.ProjectDetailsPageValidation
import com.fone.jobOpening.presentation.dto.ValidateJobOpeningDto.RolePageValidation
import com.fone.jobOpening.presentation.dto.ValidateJobOpeningDto.TitlePageValidation
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate

@IntegrationTest
class ValidateJobOpeningControllerTest(client: WebTestClient) : CustomDescribeSpec() {
    init {
        val url = "/api/v1/job-openings/validate"
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)
        describe("#JobOpening 검증 API") {
            context("title 페이지") {
                it("성공한다") {
                    val request = TitlePageValidation(
                        "제목",
                        listOf(CategoryType.ETC),
                        LocalDate.now(),
                        LocalDate.now(),
                        listOf("")
                    )
                    client.doPost("$url/title", request, accessToken)
                        .expectStatus().isOk.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("SUCCESS")
                }
                it("실패한다") {
                    val request = TitlePageValidation(
                        "",
                        listOf(CategoryType.ETC),
                        LocalDate.now(),
                        LocalDate.now(),
                        listOf("")
                    )
                    client.doPost("$url/title", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
            }
            context("ProjectDetails 페이지") {
                it("성공한다") {
                    val request = ProjectDetailsPageValidation(
                        "서울특별시",
                        "강남구",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                    client.doPost("$url/project-details", request, accessToken)
                        .expectStatus().isOk.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("SUCCESS")
                }
                it("실패한다") {
                    val request = ProjectDetailsPageValidation(
                        "서울특별시",
                        "상상구",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                    client.doPost("$url/project-details", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
            }
            context("role 페이지") {
                it("성공한다") {
                    val request = RolePageValidation(
                        "역할",
                        1,
                        Gender.MAN,
                        88,
                        0,
                        Career.NEWCOMER
                    )
                    client.doPost("$url/role", request, accessToken)
                        .expectStatus().isOk.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("SUCCESS")
                }
                it("실패한다") {
                    val request = RolePageValidation(
                        "역할",
                        null,
                        Gender.MAN,
                        88,
                        0,
                        Career.NEWCOMER
                    )
                    client.doPost("$url/role", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
            }
        }
    }
}
