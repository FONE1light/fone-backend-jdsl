package com.fone.common

import com.fone.common.response.CommonResponse
import com.fone.competition.presentation.dto.RegisterCompetitionRequest
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate

object CommonCompetitionCallApi {
    private const val registerUrl = "/api/v1/competitions"

    fun register(client: WebTestClient, accessToken: String): Long {
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
