package com.fone.jobOpening.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fone.common.CommonJobOpeningCallApi
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPut
import com.fone.common.entity.Career
import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.common.entity.Gender
import com.fone.common.entity.Type
import com.fone.common.response.CommonResponse
import com.fone.jobOpening.presentation.dto.RegisterJobOpeningDto.RegisterJobOpeningRequest
import com.fone.jobOpening.presentation.dto.RegisterJobOpeningDto.RegisterJobOpeningResponse
import com.fone.jobOpening.presentation.dto.common.LocationDto
import com.fone.jobOpening.presentation.dto.common.WorkDto
import io.kotest.matchers.shouldBe
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class PutJobOpeningControllerTest(client: WebTestClient, private val objectMapper: ObjectMapper) :
    CustomDescribeSpec() {
    private val putUrl = "/api/v1/job-openings"

    init {
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)
        val jobOpeningId = CommonJobOpeningCallApi.register(client, accessToken)

        val putJobOpeningActorRequest =
            RegisterJobOpeningRequest(
                "테스트 제목2",
                listOf(CategoryType.ETC),
                "테스트 캐스팅",
                2,
                Gender.IRRELEVANT,
                100,
                0,
                Career.IRRELEVANT,
                Type.ACTOR,
                listOf(DomainType.ART),
                WorkDto(
                    "update",
                    "update",
                    "update",
                    "update",
                    "update",
                    "update",
                    "update",
                    setOf(),
                    "update",
                    "update@email.com"
                ),
                null,
                null,
                null,
                listOf("https://www.naver.com"),
                LocationDto("서울특별시", "강서구")
            )

        describe("#put jobOpening") {
            context("존재하는 구인구직을 수정하면") {
                it("성공한다") {
                    client
                        .doPut("$putUrl/$jobOpeningId", putJobOpeningActorRequest, accessToken)
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith {
                            val opening =
                                objectMapper.readValue<CommonResponse<RegisterJobOpeningResponse>>(
                                    it.responseBody!!
                                )
                            opening.data!!.jobOpening.work shouldBe putJobOpeningActorRequest.work
                        }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }

            context("존재하지 않는 구인구직을 수정하면") {
                it("실패한다") {
                    client
                        .doPut("$putUrl/1231", putJobOpeningActorRequest, accessToken)
                        .expectStatus()
                        .isBadRequest
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("FAIL")
                }
            }
        }
    }
}
