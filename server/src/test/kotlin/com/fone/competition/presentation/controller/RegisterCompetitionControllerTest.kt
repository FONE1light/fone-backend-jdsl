package com.fone.competition.presentation.controller

import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPost
import com.fone.competition.presentation.dto.RegisterCompetitionDto.RegisterCompetitionRequest
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate

@IntegrationTest
class RegisterCompetitionControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val registerUrl = "/api/v1/competitions"

    init {
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)
        val registerCompetitionRequest = RegisterCompetitionRequest(
            title = "테스트 제목",
            imageUrl = "test url",
            exhibitStartDate = LocalDate.now(),
            exhibitEndDate = LocalDate.now(),
            screeningStartDate = LocalDate.now(),
            screeningEndDate = LocalDate.now(),
            showStartDate = LocalDate.now(),
            agency = "test agency",
            details = "test details",
            linkUrl = "test link url"
        )

        describe("#register competition") {
            context("유효한 정보로 공모전을 등록 하면") {
                it("성공한다") {
                    client
                        .doPost(registerUrl, registerCompetitionRequest, accessToken)
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
        }
    }
}
