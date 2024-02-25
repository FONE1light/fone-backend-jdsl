package com.fone.competition.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fone.common.CommonCompetitionCallApi
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.PageDeserializer
import com.fone.common.doGet
import com.fone.common.response.CommonResponse
import com.fone.competition.presentation.dto.RetrieveCompetitionsResponse
import com.fone.competition.presentation.dto.common.CompetitionDto
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.data.domain.Page
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class RetrieveCompetitionControllerTest(client: WebTestClient, private val objectMapper: ObjectMapper) :
    CustomDescribeSpec() {

    private val retrieveUrl = "/api/v1/competitions"

    init {
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)
        val competitionId = CommonCompetitionCallApi.register(client, accessToken)

        describe("#retrieve competitions") {
            context("공모전 리스트를 조회하면") {
                it("성공한다") {
                    client.doGet(retrieveUrl, accessToken).expectStatus().isOk.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("SUCCESS")
                }
            }
        }

        describe("#retrieve competition") {
            context("존재하는 공모전을 상세 조회하면") {
                it("성공한다") {
                    client.doGet("$retrieveUrl/$competitionId", accessToken)
                        .expectStatus().isOk.expectBody().consumeWith { println(it) }.jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }

            context("존재하지 않는 공모전을 상세 조회하면") {
                it("실패한다") {
                    client.doGet("$retrieveUrl/1231", accessToken)
                        .expectStatus()
                        .isBadRequest.expectBody().consumeWith { println(it) }.jsonPath("$.result")
                        .isEqualTo("FAIL")
                }
            }
        }
        describe("#retrieve competition 정렬") {
            val pageObjectMapper = objectMapper.copy()
            pageObjectMapper.registerModule(
                SimpleModule().addDeserializer(
                    Page::class.java,
                    PageDeserializer(objectMapper, CompetitionDto::class.java)
                )
            )
            context("공모전 정렬 조회") {
                it("조회가능 일자 DESC") {
                    client.doGet(retrieveUrl, accessToken, mapOf("sort" to "showStartDate,desc"))
                        .expectStatus().isOk.expectBody()
                        .consumeWith {
                            println(it)
                            val response: CommonResponse<RetrieveCompetitionsResponse> =
                                pageObjectMapper.readValue(String(it.responseBody!!))
                            val viewDates = response.data?.competitions?.toList()?.map { dto -> dto.showStartDate!! }
                            viewDates shouldNotBe null
                            viewDates shouldBe viewDates!!.sortedDescending()
                        }
                }
            }
        }
    }
}
