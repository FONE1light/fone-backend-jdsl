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
import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.common.response.CommonResponse
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningResponse
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningsResponse
import com.fone.jobOpening.presentation.dto.common.JobOpeningDto
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
                        .expectStatus().isOk.expectBody().consumeWith { println(it) }.jsonPath("$.result")
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

            context("Location 정보가 있음") {
                it("성공한다") {
                    client.doGet("$retrieveUrl/$jobOpeningId", accessToken, mapOf("type" to "ACTOR"))
                        .expectStatus().isOk.expectBody()
                        .consumeWith {
                            val response =
                                objectMapper.readValue<CommonResponse<RetrieveJobOpeningResponse>>(
                                    it.responseBody!!
                                )
                            response.data!!.jobOpening.fifthPage.workingCity shouldBe "서울특별시"
                            response.data!!.jobOpening.fifthPage.workingDistrict shouldBe "도봉구"
                        }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
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
        describe("#retrieve jobOpenings with filters") {
            context("조건에 맞는 구인구직 리스트를 조회하면") {
                it("성공한다") {
                    val filterParams = mapOf(
                        "type" to "ACTOR",
                        "ageMin" to "20",
                        "ageMax" to "30"
                    )
                    client.doGet(retrieveUrl, accessToken, filterParams)
                        .expectStatus().isOk.expectBody().consumeWith { println(it) }
                        .jsonPath("$.result").isEqualTo("SUCCESS")
                }
            }
        }
        describe("#retrieve jobOpenings sorted by viewCount") {
            context("조회수(viewCount)로 정렬된 구인구직 리스트를 조회하면") {
                it("성공한다") {
                    client.doGet(retrieveUrl, accessToken, mapOf("type" to "ACTOR", "sort" to "viewCount,desc"))
                        .expectStatus().isOk.expectBody().consumeWith { println(it) }
                        .jsonPath("$.result").isEqualTo("SUCCESS")
                }
            }
        }
        describe("#retrieve jobOpenings sorted by createdAt") {
            context("생성일(createdAt)로 정렬된 구인구직 리스트를 조회하면") {
                it("성공한다") {
                    client.doGet(retrieveUrl, accessToken, mapOf("type" to "ACTOR", "sort" to "createdAt,asc"))
                        .expectStatus().isOk.expectBody().consumeWith { println(it) }
                        .jsonPath("$.result").isEqualTo("SUCCESS")
                }
            }
        }
        describe("#retrieve jobOpenings sorted by scrapCount") {
            context("스크랩수(scrapCount)로 정렬된 구인구직 리스트를 조회하면") {
                it("성공한다") {
                    client.doGet(retrieveUrl, accessToken, mapOf("type" to "ACTOR", "sort" to "scrapCount,desc"))
                        .expectStatus().isOk.expectBody().consumeWith { println(it) }
                        .jsonPath("$.result").isEqualTo("SUCCESS")
                }
            }
        }
        describe("#retrieve jobOpenings sorted by recruitmentEndDate") {
            context("모집 종료일(recruitmentEndDate)로 정렬된 구인구직 리스트를 조회하면") {
                it("성공한다") {
                    client.doGet(retrieveUrl, accessToken, mapOf("type" to "ACTOR", "sort" to "recruitmentEndDate,asc"))
                        .expectStatus().isOk.expectBody().consumeWith { println(it) }
                        .jsonPath("$.result").isEqualTo("SUCCESS")
                }
            }
        }
        describe("#retrieve jobOpenings with domains and categories") {
            context("특정 분야(domains)와 카테고리(categories)로 필터링된 구인구직 리스트를 조회하면") {
                it("성공한다") {
                    val params = mapOf(
                        "type" to "ACTOR",
                        "domains" to listOf(DomainType.SCENARIO).joinToString(","),
                        "categories" to listOf(CategoryType.WEB_DRAMA).joinToString(",")
                    )
                    client.doGet(retrieveUrl, accessToken, params)
                        .expectStatus().isOk.expectBody().consumeWith { println(it) }
                        .jsonPath("$.result").isEqualTo("SUCCESS")
                }
            }
        }
    }
}
