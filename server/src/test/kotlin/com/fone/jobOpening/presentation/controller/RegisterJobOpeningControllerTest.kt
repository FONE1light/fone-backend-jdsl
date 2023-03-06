package com.fone.jobOpening.presentation.controller

import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPost
import com.fone.common.entity.Career
import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.common.entity.Gender
import com.fone.common.entity.Type
import com.fone.jobOpening.presentation.dto.RegisterJobOpeningDto.RegisterJobOpeningRequest
import com.fone.jobOpening.presentation.dto.common.WorkDto
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate

@IntegrationTest
class RegisterJobOpeningControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val registerUrl = "/api/v1/job-openings"

    init {
        val (accessToken, email) = CommonUserCallApi.getAccessToken(client)
        val registerJobOpeningActorRequest = RegisterJobOpeningRequest(
            "테스트 제목",
            listOf(CategoryType.ETC),
            LocalDate.now(),
            "테스트 캐스팅",
            2,
            Gender.IRRELEVANT,
            100,
            0,
            Career.IRRELEVANT,
            Type.ACTOR,
            listOf(DomainType.ART),
            WorkDto(
                "", "", "", "", "", "", "", "", "", "", ""
            )
        )

        val registerJobOpeningStaffRequest = RegisterJobOpeningRequest(
            "테스트 제목",
            listOf(CategoryType.ETC),
            LocalDate.now(),
            "테스트 캐스팅",
            2,
            Gender.IRRELEVANT,
            100,
            0,
            Career.IRRELEVANT,
            Type.STAFF,
            listOf(DomainType.ART),
            WorkDto(
                "", "", "", "", "", "", "", "", "", "", ""
            )
        )

        describe("#register jobOpening") {
            context("유효한 정보로 배우 구인구직을 등록 하면") {
                it("성공한다") {
                    client.doPost(registerUrl, registerJobOpeningActorRequest, accessToken)
                        .expectStatus().isOk.expectBody().consumeWith { println(it) }.jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }

            context("유효한 정보로 스태프 구인구직을 등록 하면") {
                it("성공한다") {
                    client.doPost(registerUrl, registerJobOpeningStaffRequest, accessToken)
                        .expectStatus().isOk.expectBody().consumeWith { println(it) }.jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
        }
    }
}
