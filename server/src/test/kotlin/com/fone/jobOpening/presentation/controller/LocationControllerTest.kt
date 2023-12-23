package com.fone.jobOpening.presentation.controller

import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doGet
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class LocationControllerTest(client: WebTestClient) : CustomDescribeSpec() {
    init {
        val url = "/api/v1/job-openings/locations"
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)
        describe("#get 지역 정보 조회") {
            context("지역 조회") {
                it("성공한다") {
                    client.doGet("$url/regions", accessToken).expectStatus().isOk.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("SUCCESS")
                }
            }

            context("시군구 조회") {
                it("성공한다") {
                    client.doGet("$url/districts/서울특별시", accessToken).expectStatus().isOk.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("SUCCESS")
                }
            }
        }
    }
}
