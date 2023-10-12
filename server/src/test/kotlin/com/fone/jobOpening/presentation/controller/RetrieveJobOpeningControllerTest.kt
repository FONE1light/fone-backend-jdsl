package com.fone.jobOpening.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fone.common.CommonJobOpeningCallApi
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.PageDeserializer
import com.fone.common.doGet
import com.fone.common.response.CommonResponse
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningDto.RetrieveJobOpeningResponse
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningDto.RetrieveJobOpeningsResponse
import com.fone.jobOpening.presentation.dto.common.JobOpeningDto
import com.fone.user.domain.enum.Job
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.data.domain.Page
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class RetrieveJobOpeningControllerTest(client: WebTestClient, private val objectMapper: ObjectMapper) :
    CustomDescribeSpec() {

    private val retrieveUrl = "/api/v1/job-openings"

    init {
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)
        val jobOpeningId = CommonJobOpeningCallApi.register(client, accessToken)

        describe("#retrieve jobOpenings") {
            context("구인구직 리스트를 조회하면") {
                it("성공한다") {
                    client.doGet(retrieveUrl, accessToken, mapOf("type" to "ACTOR")).expectStatus().isOk.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("SUCCESS")
                }
            }
        }

        describe("#retrieve jobOpening") {
            context("존재하는 구인구직을 상세 조회하면") {
                it("성공한다") {
                    client.doGet("$retrieveUrl/$jobOpeningId", accessToken, mapOf("type" to "ACTOR"))
                        .expectStatus().isOk.expectBody().consumeWith {
                            val response =
                                objectMapper.readValue<CommonResponse<RetrieveJobOpeningResponse>>(
                                    it.responseBody!!
                                )
                            response.data!!.jobOpening.userJob shouldBe Job.VERIFIED
                        }.jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }

            context("존재하지 않는 구인구직을 상세 조회하면") {
                it("실패한다") {
                    client.doGet("$retrieveUrl/1231", accessToken, mapOf("type" to "ACTOR"))
                        .expectStatus()
                        .isBadRequest.expectBody().consumeWith { println(it) }.jsonPath("$.result")
                        .isEqualTo("FAIL")
                }
            }
        }
        describe("#retrieve jobOpening 정렬") {
            val pageObjectMapper = objectMapper.copy()
            pageObjectMapper.registerModule(
                SimpleModule().addDeserializer(
                    Page::class.java,
                    PageDeserializer(objectMapper, JobOpeningDto::class.java)
                )
            )
            context("구인구직 정렬 조회") {
                it("조회순 DESC") {
                    client.doGet(retrieveUrl, accessToken, mapOf("type" to "ACTOR", "sort" to "viewCount,desc"))
                        .expectStatus().isOk.expectBody().consumeWith {
                            println(it)
                            val response: CommonResponse<RetrieveJobOpeningsResponse> =
                                pageObjectMapper.readValue(String(it.responseBody!!))
                            val viewCounts = response.data?.jobOpenings?.toList()?.map { dto -> dto.viewCount }
                            viewCounts shouldNotBe null
                            viewCounts shouldBe viewCounts!!.sortedDescending()
                            objectMapper
                        }
                }
            }
        }
    }
}
