package com.fone.jobOpening.presentation.controller

import com.fone.common.CommonJobOpeningCallApi
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPut
import com.fone.common.entity.Career
import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.common.entity.Gender
import com.fone.common.entity.Type
import com.fone.jobOpening.presentation.dto.RegisterJobOpeningDto
import com.fone.jobOpening.presentation.dto.common.WorkDto
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate

@IntegrationTest
class PutJobOpeningControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val putUrl = "/api/v1/job-openings"

    init {
        val (accessToken, email) = CommonUserCallApi.getAccessToken(client)
        val jobOpeningId = CommonJobOpeningCallApi.register(client, accessToken)

        val putJobOpeningActorRequest =
            RegisterJobOpeningDto.RegisterJobOpeningRequest(
                "테스트 제목2",
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
                    ""
                )
            )

        describe("#put jobOpening") {
            context("존재하는 구인구직을 수정하면") {
                it("성공한다") {
                    client
                        .doPut("$putUrl/$jobOpeningId", putJobOpeningActorRequest, accessToken)
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }

            context("존재하지 않는 구인구직을 수정하면") {
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
