package com.fone.report.presentation.controller

import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPost
import com.fone.report.domain.enum.Type
import com.fone.report.presentation.dto.RegisterReportDto.RegisterReportRequest
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class RegisterReportControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val registerUrl = "/api/v1/reports"

    init {
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)
        val registerReportRequest = RegisterReportRequest(
            reportUserId = 1,
            type = Type.CHATTING,
            typeId = 1,
            inconveniences = listOf(""),
            details = "test details"
        )

        describe("#register report") {
            context("유효한 정보로 신고하기를 등록하면") {
                it("성공한다") {
                    client
                        .doPost(registerUrl, registerReportRequest, accessToken)
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
