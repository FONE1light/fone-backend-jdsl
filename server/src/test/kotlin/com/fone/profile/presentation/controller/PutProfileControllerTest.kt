package com.fone.profile.presentation.controller

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
import com.fone.profile.presentation.dto.RegisterProfileDto
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate

@IntegrationTest
class PutProfileControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val putUrl = "/api/v1/profiles"

    init {
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)
        val profileId = CommonProfileCallApi.register(client, accessToken)

        val putJobOpeningActorRequest = RegisterProfileDto.RegisterProfileRequest(
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
            careers = setOf(Career.LESS_THAN_6YEARS, Career.LESS_THAN_10YEARS),
            careerDetail = "test",
            categories = listOf(CategoryType.ETC),
            type = Type.ACTOR,
            domains = listOf(DomainType.PAINTING),
            profileUrls = listOf("test profile url"),
            profileUrl = "test profile url"
        )

        describe("#put profile") {
            context("존재하는 프로필을 수정하면") {
                it("성공한다") {
                    client
                        .doPut("$putUrl/$profileId", putJobOpeningActorRequest, accessToken)
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }

            context("존재하지 않는 프로필을 수정하면") {
                it("실패한다") {
                    client
                        .doPut("$putUrl/1231", putJobOpeningActorRequest, accessToken)
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("FAIL")
                }
            }
        }
    }
}
