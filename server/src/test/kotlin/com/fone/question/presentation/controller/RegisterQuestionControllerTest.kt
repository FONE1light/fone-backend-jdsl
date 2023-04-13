package com.fone.question.presentation.controller

import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPost
import com.fone.question.domain.enum.Type
import com.fone.question.presentation.dto.RegisterQuestionDto.RegisterQuestionRequest
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class RegisterQuestionControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val registerUrl = "/api/v1/questions"

    init {
        val (_, email) = CommonUserCallApi.getAccessToken(client)
        val registerQuestionRequest =
            RegisterQuestionRequest(email, Type.ALLIANCE, "테스트 제목", "테스트 설명", true)

        describe("#register question") {
            context("유효한 정보로 문의등록을 하면") {
                it("성공한다") {
                    client
                        .doPost(registerUrl, registerQuestionRequest)
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
