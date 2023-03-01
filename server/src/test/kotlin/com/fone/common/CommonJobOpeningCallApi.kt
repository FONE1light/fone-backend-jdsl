package com.fone.common

import com.fone.common.entity.*
import com.fone.common.response.CommonResponse
import com.fone.jobOpening.presentation.dto.RegisterJobOpeningDto
import com.fone.jobOpening.presentation.dto.common.WorkDto
import java.time.LocalDate
import org.springframework.test.web.reactive.server.WebTestClient

object CommonJobOpeningCallApi {
    private const val registerUrl = "/api/v1/job-openings"

    fun register(client: WebTestClient, accessToken: String): Long {
        val registerJobOpeningActorRequest =
            RegisterJobOpeningDto.RegisterJobOpeningRequest(
                "테스트 제목",
                listOf(CategoryType.ETC),
                LocalDate.now(),
                "테스트 캐스팅",
                2,
                Gender.IRRELEVANT,
                100,
                0,
                Career.IRRELEVANT,
                Type.ACTOR,
                listOf(DomainType.ART),
                WorkDto(
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                ),
            )

        val jobOpening =
            (client
                .doPost(registerUrl, registerJobOpeningActorRequest, accessToken)
                .expectStatus()
                .isOk
                .expectBody(CommonResponse::class.java)
                .returnResult()
                .responseBody
                ?.data as LinkedHashMap<*, *>)["jobOpening"]

        val jobOpeningId = (jobOpening as LinkedHashMap<*, *>)["id"]

        return jobOpeningId.toString().toLong()
    }
}
