package com.fone.jobOpening.presentation.controller

import com.fone.common.CommonJobOpeningCallApi
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doGet
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class RetrieveJobOpeningMyRegistrationControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val retrieveMyRegistrationsUrl = "/api/v1/job-openings/my-registrations"

    init {
        val (accessToken, email) = CommonUserCallApi.getAccessToken(client)
        CommonJobOpeningCallApi.register(client, accessToken)

        describe("#retrieve jobOpenings myRegistrations") {
            context("내가 등록한 구인구직을 조회하면") {
                it("성공한다") {
                    client.doGet(retrieveMyRegistrationsUrl, accessToken).expectStatus().isOk.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("SUCCESS")
                }
            }
        }
    }
}
