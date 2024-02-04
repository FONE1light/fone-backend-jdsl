package com.fone.profile.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fone.common.CommonProfileCallApi
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPut
import com.fone.common.response.CommonResponse
import com.fone.profile.domain.entity.ProfileSns
import com.fone.profile.domain.enum.SNS
import com.fone.profile.domain.repository.ProfileRepository
import com.fone.profile.presentation.dto.RegisterProfileDto
import com.fone.profile.presentation.dto.common.ProfileSnsUrl
import com.fone.profile.presentation.dto.common.toDto
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class PutProfileControllerTest(
    objectMapper: ObjectMapper,
    profileRepository: ProfileRepository,
    client: WebTestClient,
) : CustomDescribeSpec() {

    private val putUrl = "/api/v1/profiles"

    init {
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)
        val profileId = CommonProfileCallApi.register(client, accessToken)
        val snsUrls = listOf(
            ProfileSnsUrl("https://www.instagram.com/test", SNS.INSTAGRAM),
            ProfileSnsUrl("https://www.youtube.com/test", SNS.YOUTUBE)
        )

        val putProfileActorRequest = CommonProfileCallApi.registerProfileActorRequest

        describe("#put profile") {
            context("존재하는 프로필을 수정하면") {
                it("성공한다") {
                    client
                        .doPut("$putUrl/$profileId", putProfileActorRequest, accessToken)
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith {
                            val response =
                                objectMapper.readValue<CommonResponse<RegisterProfileDto.RegisterProfileResponse>>(
                                    it.responseBody!!
                                )
                            response.data!!.profile.thirdPage.snsUrls.toSet() shouldBe snsUrls.toSet()
                            runBlocking {
                                profileRepository.findById(profileId)!!.snsUrls.map(ProfileSns::toDto)
                                    .toSet() shouldBe snsUrls.toSet()
                            }
                        }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }

            context("존재하지 않는 프로필을 수정하면") {
                it("실패한다") {
                    client
                        .doPut("$putUrl/1231", putProfileActorRequest, accessToken)
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
