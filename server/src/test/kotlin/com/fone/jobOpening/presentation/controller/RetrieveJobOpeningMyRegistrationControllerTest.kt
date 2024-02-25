package com.fone.jobOpening.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fone.common.CommonJobOpeningCallApi
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.PageDeserializer
import com.fone.common.doDelete
import com.fone.common.doGet
import com.fone.common.response.CommonResponse
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningMyRegistrationResponse
import com.fone.jobOpening.presentation.dto.common.JobOpeningDto
import io.kotest.matchers.shouldBe
import org.springframework.data.domain.Page
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class RetrieveJobOpeningMyRegistrationControllerTest(
    client: WebTestClient,
    objectMapper: ObjectMapper,
) : CustomDescribeSpec() {

    private val retrieveMyRegistrationsUrl = "/api/v1/job-openings/my-registrations"

    init {
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)
        CommonJobOpeningCallApi.register(client, accessToken)
        var id = 0L
        val pageObjectMapper = objectMapper.copy()
        pageObjectMapper.registerModule(
            SimpleModule().addDeserializer(
                Page::class.java,
                PageDeserializer(objectMapper, JobOpeningDto::class.java)
            )
        )
        describe("#retrieve jobOpenings myRegistrations") {
            context("내가 등록한 구인구직을 조회하면") {
                it("성공한다") {
                    client.doGet(retrieveMyRegistrationsUrl, accessToken).expectStatus().isOk.expectBody()
                        .consumeWith {
                            val response =
                                pageObjectMapper.readValue<CommonResponse<RetrieveJobOpeningMyRegistrationResponse>>(
                                    it.responseBody!!
                                )
                            id = response.data!!.jobOpenings.first().id
                        }.jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
            context("내가 등록한 구인구직을 제거하면") {
                it("조회가 되지 않는다.") {
                    client.doDelete("/api/v1/job-openings/$id", accessToken).expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith {
                            println(it)
                        }.jsonPath("$.result").isEqualTo("SUCCESS")
                    client.doGet(retrieveMyRegistrationsUrl, accessToken).expectStatus().isOk.expectBody()
                        .consumeWith {
                            val response =
                                pageObjectMapper.readValue<CommonResponse<RetrieveJobOpeningMyRegistrationResponse>>(
                                    it.responseBody!!
                                )
                            response.data!!.jobOpenings.totalElements shouldBe 0
                        }.jsonPath("$.result").isEqualTo("SUCCESS")
                }
            }
        }
    }
}
