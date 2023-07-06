package com.fone.jobOpening.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fone.common.CommonJobOpeningCallApi
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPost
import com.fone.common.response.CommonResponse
import com.fone.jobOpening.presentation.dto.ScrapJobOpeningDto
import io.kotest.matchers.shouldBe
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class ScrapJobOpeningControllerTest(client: WebTestClient, private val objectMapper: ObjectMapper) :
    CustomDescribeSpec() {

    private val scrapUrl = "/api/v1/job-openings"

    init {
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)
        val jobOpeningId = CommonJobOpeningCallApi.register(client, accessToken)

        describe("#scrap jobOpening") {
            context("존재하는 구인구직을 스크랩 하면") {
                it("성공한다") {
                    client
                        .doPost("$scrapUrl/$jobOpeningId/scrap", null, accessToken)
                        .expectStatus().isOk
                        .expectBody()
                        .consumeWith {
                            println(it)
                            val response =
                                objectMapper.readValue<CommonResponse<ScrapJobOpeningDto.ScrapJobOpeningResponse>>(
                                    it.responseBody!!
                                )
                            response.data!!.result shouldBe ScrapJobOpeningDto.ScrapJobResult.SCRAPPED
                        }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
            context("기존 스크랩을 다시 호출하면") {
                it("스크랩 해제된다") {
                    client
                        .doPost("$scrapUrl/$jobOpeningId/scrap", null, accessToken)
                        .expectStatus().isOk
                        .expectBody()
                        .consumeWith {
                            println(it)
                            val response =
                                objectMapper.readValue<CommonResponse<ScrapJobOpeningDto.ScrapJobOpeningResponse>>(
                                    it.responseBody!!
                                )
                            response.data!!.result shouldBe ScrapJobOpeningDto.ScrapJobResult.DISCARDED
                        }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
            context("존재하지 않는 구인구직을 스크랩 하면") {
                it("실패한다") {
                    client
                        .doPost("$scrapUrl/1231/scrap", null, accessToken)
                        .expectStatus().isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("FAIL")
                }
            }
        }
    }
}
