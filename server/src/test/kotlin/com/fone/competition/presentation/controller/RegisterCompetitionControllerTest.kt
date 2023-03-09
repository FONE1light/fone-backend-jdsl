package com.fone.competition.presentation.controller

import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPost
import com.fone.competition.presentation.dto.RegisterCompetitionDto.PrizeRequest
import com.fone.competition.presentation.dto.RegisterCompetitionDto.RegisterCompetitionRequest
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate

@IntegrationTest
class RegisterCompetitionControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val registerUrl = "/api/v1/competitions"

    init {
        val (accessToken, email) = CommonUserCallApi.getAccessToken(client)
        val registerCompetitionRequest = RegisterCompetitionRequest(
            title = "테스트 제목",
            imageUrl = "test url",
            startDate = LocalDate.now(),
            endDate = LocalDate.now(),
            submitStartDate = LocalDate.now(),
            submitEndDate = LocalDate.now(),
            showStartDate = LocalDate.now(),
            agency = "test agency",
            details = "test details",
            prizes = listOf(
                PrizeRequest(
                    ranking = "",
                    prizeMoney = "",
                    competitionId = 0
                )
            )
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
