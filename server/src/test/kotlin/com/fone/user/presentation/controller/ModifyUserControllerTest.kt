package com.fone.user.presentation.controller

import com.fone.common.CommonCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPatch
import com.fone.common.entity.CategoryType
import com.fone.user.domain.enum.Job
import com.fone.user.presentation.dto.ModifyUserDto
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class ModifyUserControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val modifyUrl = "/api/v1/users"

    init {
        val (accessToken, _) = CommonCallApi.getAccessToken(client)
        val modifyUserRequest =
            ModifyUserDto.ModifyUserRequest(
                Job.ACTOR,
                listOf(CategoryType.ETC),
                "test1515151",
                "",
            )

        describe("#modify") {
            context("존재하는 유저의 정보로 수정을 요청하면") {
                it("성공한다") {
                    client
                        .doPatch(modifyUrl, modifyUserRequest, accessToken)
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
