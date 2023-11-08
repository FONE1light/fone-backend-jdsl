package com.fone.etc

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.readValue
import com.fone.common.CommonProfileCallApi
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
import com.fone.profile.presentation.dto.RegisterProfileDto.RegisterProfileRequest
import io.kotest.matchers.string.shouldContain
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate

@IntegrationTest
class HttpModelFailureResponseTest(
    client: WebTestClient,
    private val objectMapper: ObjectMapper,
) : CustomDescribeSpec() {

    private val putUrl = "/api/v1/profiles"

    init {
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)
        val profileId = CommonProfileCallApi.register(client, accessToken)

        val putJobOpeningActorRequest = RegisterProfileRequest(
            name = "테스트 이름",
            hookingComment = "테스트 후킹 멘트",
            birthday = LocalDate.now(),
            gender = Gender.IRRELEVANT,
            height = 180,
            weight = 70,
            email = "test12345@test.com",
            sns = "test sns",
            specialty = "test",
            details = "test",
            career = Career.IRRELEVANT,
            careerDetail = "test",
            categories = listOf(CategoryType.ETC),
            type = Type.ACTOR,
            domains = listOf(DomainType.PAINTING),
            profileImages = listOf("test profile url"),
            mainProfileImage = "test profile url"
        )

        describe("#실패 요청") {
            context("PUT 요청에 필수 필드 빠지면") {
                it("실패하고 빠진 필드 정보 돌려준다") {
                    val requestBody = objectMapper.convertValue<MutableMap<String, Any>>(putJobOpeningActorRequest)
                    requestBody.remove("type") // request에 Type 제거
                    client
                        .doPut("$putUrl/$profileId", requestBody, accessToken)
                        .expectStatus()
                        .isBadRequest
                        .expectBody()
                        .consumeWith {
                            val response: CommonResponse<String> = objectMapper.readValue(it.responseBody!!)
                            println(response)
                            response.data shouldContain "type"
                        }
                }
            }
        }
    }
}
