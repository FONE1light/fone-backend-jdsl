package com.fone.common

import com.fone.common.response.CommonResponse
import com.fone.competition.presentation.dto.RegisterCompetitionDto
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate

object CommonCompetitionCallApi {
    private const val registerUrl = "/api/v1/competitions"

    fun register(client: WebTestClient, accessToken: String): Long {
        val registerCompetitionRequest = RegisterCompetitionDto.RegisterCompetitionRequest(
            title = "테스트 제목",
            imageUrl = "test url",
            startDate = LocalDate.now(),
            endDate = LocalDate.now(),
            submitStartDate = LocalDate.now(),
            submitEndDate = LocalDate.now(),
            showStartDate = LocalDate.now(),
            agency = "test agency",
            details = "test details"
        )

        val competition =
            (
                client.doPost(registerUrl, registerCompetitionRequest, accessToken)
                    .expectStatus()
                    .isOk
                    .expectBody(CommonResponse::class.java)
                    .returnResult()
                    .responseBody?.data as LinkedHashMap<*, *>
                )["competition"]

        val competitionId = (competition as LinkedHashMap<*, *>)["id"]

        return competitionId.toString().toLong()
    }
}
