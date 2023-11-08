package com.fone.profile.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPost
import com.fone.common.entity.Career
import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.common.entity.Gender
import com.fone.common.entity.Type
import com.fone.common.response.CommonResponse
import com.fone.profile.domain.enum.SNS
import com.fone.profile.presentation.dto.RegisterProfileDto
import com.fone.profile.presentation.dto.RegisterProfileDto.RegisterProfileRequest
import com.fone.profile.presentation.dto.common.ProfileSnsUrl
import io.kotest.matchers.shouldBe
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate

@IntegrationTest
class RegisterProfileControllerTest(objectMapper: ObjectMapper, client: WebTestClient) : CustomDescribeSpec() {

    private val registerUrl = "/api/v1/profiles"

    init {
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)
        val snsUrls = listOf(
            ProfileSnsUrl("https://www.instagram.com", SNS.INSTAGRAM),
            ProfileSnsUrl("https://www.youtube.com/", SNS.YOUTUBE)
        )

        val registerProfileActorRequest = RegisterProfileRequest(
            name = "테스트 이름",
            hookingComment = "테스트 후킹 멘트",
            birthday = LocalDate.now(),
            gender = Gender.IRRELEVANT,
            height = 180,
            weight = 70,
            email = "test12345@test.com",
            specialty = "test",
            details = "test",
            career = Career.IRRELEVANT,
            careerDetail = "test",
            categories = listOf(CategoryType.ETC),
            type = Type.ACTOR,
            domains = listOf(DomainType.PAINTING),
            profileUrls = listOf("test profile url"),
            profileUrl = "test profile url",
            snsUrls = snsUrls
        )

        val registerProfileStaffRequest = RegisterProfileRequest(
            name = "테스트 이름",
            hookingComment = "테스트 후킹 멘트",
            birthday = LocalDate.now(),
            gender = Gender.IRRELEVANT,
            height = 180,
            weight = 70,
            email = "test12345@test.com",
            specialty = "test",
            details = "test",
            career = Career.IRRELEVANT,
            careerDetail = "test",
            categories = listOf(CategoryType.ETC),
            type = Type.ACTOR,
            domains = listOf(DomainType.PAINTING),
            profileUrls = listOf("test profile url"),
            profileUrl = "test profile url",
            snsUrls = snsUrls
        )

        describe("#register profile") {
            context("유효한 정보로 배우 프로필을 등록 하면") {
                it("성공한다") {
                    client.doPost(registerUrl, registerProfileActorRequest, accessToken)
                        .expectStatus().isOk.expectBody()
                        .consumeWith {
                            val response =
                                objectMapper.readValue<CommonResponse<RegisterProfileDto.RegisterProfileResponse>>(
                                    it.responseBody!!
                                )
                            response.data!!.profile.snsUrls.toSet() shouldBe snsUrls.toSet()
                        }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }

            context("유효한 정보로 스태프 프로필을 등록 하면") {
                it("성공한다") {
                    client.doPost(registerUrl, registerProfileStaffRequest, accessToken)
                        .expectStatus().isOk.expectBody().consumeWith { println(it) }.jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
        }
    }
}
