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

    private val guestRegisterUrl = "/api/v1/questions"
    private val userRegisterUrl = "/api/v1/questions/user"

    init {
        val (token, email) = CommonUserCallApi.getAccessToken(client)
        val registerQuestionRequest =
            RegisterQuestionRequest(email, Type.ALLIANCE, "테스트 제목", "테스트 설명", true)

        describe("#register question") {
            context("비회원이 유효한 정보로 문의등록을 하면") {
                it("성공한다") {
                    client
                        .doPost(guestRegisterUrl, registerQuestionRequest)
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
            context("회원이 유효한 정보로 문의등록을 하면") {
                it("성공한다") {
                    client
                        .doPost(userRegisterUrl, registerQuestionRequest, token)
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
