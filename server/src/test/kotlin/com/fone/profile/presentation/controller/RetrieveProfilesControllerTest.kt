package com.fone.profile.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.readValue
import com.fone.common.CommonProfileCallApi
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.PageDeserializer
import com.fone.common.doGet
import com.fone.common.response.CommonResponse
import com.fone.profile.presentation.dto.RetrieveProfilesDto
import com.fone.profile.presentation.dto.common.ProfileDto
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.data.domain.Page
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class RetrieveProfilesControllerTest(client: WebTestClient, private val objectMapper: ObjectMapper) :
    CustomDescribeSpec() {

    private val retrieveUrl = "/api/v1/profiles"

    init {
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)
        val profileId = CommonProfileCallApi.register(client, accessToken)

        describe("#retrieve profiles") {
            context("프로필 리스트를 조회하면") {
                it("성공한다") {
                    client
                        .doGet(retrieveUrl, accessToken, mapOf("type" to "ACTOR"))
                        .expectStatus().isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
                it("Career 여러 개 있다") {
                    client
                        .doGet(retrieveUrl, accessToken, mapOf("type" to "ACTOR"))
                        .expectStatus().isOk
                        .expectBody()
                        .consumeWith {
                            val body =
                                objectMapper.readValue<CommonResponse<Map<Any, Any>>>(
                                    it.responseBody!!
                                )
                            val profiles =
                                objectMapper.convertValue<List<ProfileDto>>(
                                    (body.data!!["profiles"] as Map<String, Any>)["content"]!!
                                )
                            profiles.shouldExist { profile -> profile.careers.size > 1 }
                        }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
        }

        describe("#retrieve profile") {
            context("존재하는 프로필을 상세 조회하면") {
                it("성공한다") {
                    client
                        .doGet("$retrieveUrl/$profileId", accessToken, mapOf("type" to "ACTOR"))
                        .expectStatus().isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }

            context("존재하지 않는 프로필을 상세 조회하면") {
                it("실패한다") {
                    client
                        .doGet("$retrieveUrl/1231", accessToken, mapOf("type" to "ACTOR"))
                        .expectStatus().isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("FAIL")
                }
            }

            context("프로파일 정렬 조회") {
                val pageObjectMapper = objectMapper.copy()
                pageObjectMapper.registerModule(
                    SimpleModule().addDeserializer(
                        Page::class.java,
                        PageDeserializer(objectMapper, ProfileDto::class.java)
                    )
                )
                it("생성시간 ASC") {
                    client
                        .doGet(retrieveUrl, accessToken, mapOf("type" to "ACTOR", "sort" to "createdAt"))
                        .expectStatus().isOk
                        .expectBody()
                        .consumeWith {
                            val response: CommonResponse<RetrieveProfilesDto.RetrieveProfilesResponse> =
                                pageObjectMapper.readValue(String(it.responseBody!!))
                            println(response)
                            val createDates = response.data?.profiles?.toList()?.map { dto -> dto.createdAt }
                            createDates shouldNotBe null
                            createDates shouldBe createDates!!.sorted()
                        }
                }

                it("생성시간 DESC") {
                    client
                        .doGet(retrieveUrl, accessToken, mapOf("type" to "ACTOR", "sort" to "createdAt,desc"))
                        .expectStatus().isOk
                        .expectBody()
                        .consumeWith {
                            val response: CommonResponse<RetrieveProfilesDto.RetrieveProfilesResponse> =
                                pageObjectMapper.readValue(String(it.responseBody!!))
                            println(response)
                            val createDates = response.data?.profiles?.toList()?.map { dto -> dto.createdAt }
                            createDates shouldNotBe null
                            createDates shouldBe createDates!!.sortedDescending()
                        }
                }

                it("조회순 DESC") {
                    client
                        .doGet(retrieveUrl, accessToken, mapOf("type" to "ACTOR", "sort" to "viewCount,desc"))
                        .expectStatus().isOk
                        .expectBody()
                        .consumeWith {
                            val response: CommonResponse<RetrieveProfilesDto.RetrieveProfilesResponse> =
                                pageObjectMapper.readValue(String(it.responseBody!!))
                            println(response)
                            val viewCounts = response.data?.profiles?.toList()?.map { dto -> dto.viewCount }
                            viewCounts shouldNotBe null
                            viewCounts shouldBe viewCounts!!.sortedDescending()
                        }
                }
            }
        }
    }
}
