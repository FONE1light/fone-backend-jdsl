package com.fone.jobOpening.presentation.controller

import com.fone.common.CommonJobOpeningCallApi
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPost
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class RegisterJobOpeningControllerTest(client: WebTestClient) : CustomDescribeSpec() {
    private val registerUrl = "/api/v1/job-openings"

    init {
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)
        val registerJobOpeningActorRequest = CommonJobOpeningCallApi.registerJobOpeningActorRequest
        val registerJobOpeningStaffRequest = CommonJobOpeningCallApi.registerJobOpeningStaffRequest

        describe("#register jobOpening") {
            context("유효한 정보로 배우 구인구직을 등록 하면") {
                it("성공한다") {
                    client.doPost(registerUrl, registerJobOpeningActorRequest, accessToken)
                        .expectStatus().isOk.expectBody().consumeWith { println(it) }.jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }

            context("유효한 정보로 스태프 구인구직을 등록 하면") {
                it("성공한다") {
                    client.doPost(registerUrl, registerJobOpeningStaffRequest, accessToken)
                        .expectStatus().isOk.expectBody().consumeWith { println(it) }.jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
        }
    }
}
