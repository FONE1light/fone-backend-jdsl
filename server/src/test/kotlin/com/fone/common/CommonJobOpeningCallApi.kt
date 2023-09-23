package com.fone.common

import com.fone.common.entity.Career
import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.common.entity.Gender
import com.fone.common.entity.Type
import com.fone.common.response.CommonResponse
import com.fone.jobOpening.presentation.dto.RegisterJobOpeningDto
import com.fone.jobOpening.presentation.dto.common.WorkDto
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate

object CommonJobOpeningCallApi {
    private const val registerUrl = "/api/v1/job-openings"

    fun register(client: WebTestClient, accessToken: String): Long {
        val registerJobOpeningActorRequest = RegisterJobOpeningDto.RegisterJobOpeningRequest(
            "테스트 제목",
            listOf(CategoryType.ETC),
            LocalDate.now(),
            "테스트 캐스팅",
            2,
            Gender.IRRELEVANT,
            100,
            0,
            listOf(Career.IRRELEVANT),
            Type.ACTOR,
            listOf(DomainType.ART),
            WorkDto(
                "", "", "", "", "", "", "", "", "", "", ""
            )
        )

        val jobOpening =
            (
                client.doPost(registerUrl, registerJobOpeningActorRequest, accessToken).expectStatus().isOk.expectBody(
                    CommonResponse::class.java
                ).returnResult().responseBody?.data as LinkedHashMap<*, *>
                )["jobOpening"]

        val jobOpeningId = (jobOpening as LinkedHashMap<*, *>)["id"]

        return jobOpeningId.toString().toLong()
    }
}
