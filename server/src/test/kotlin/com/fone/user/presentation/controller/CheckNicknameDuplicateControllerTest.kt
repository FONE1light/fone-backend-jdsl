package com.fone.user.presentation.controller

import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doGet
import com.fone.common.doPost
import com.fone.common.entity.CategoryType
import com.fone.common.entity.Gender
import com.fone.user.domain.enum.Job
import com.fone.user.domain.enum.SocialLoginType
import com.fone.user.presentation.dto.SignUpUserDto
import java.time.LocalDate
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class CheckNicknameDuplicateControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val signUpBaseUrl = "/api/v1/users/sign-up"
    private val checkNicknameDuplicateUrl = "/api/v1/users/check-nickname-duplication"

    init {
        val signUpNickname = given(client)

        describe("#checkNicknameDuplicate") {
            context("존재하지 않는 닉네임을 입력하면") {
                it("성공한다.") {
                    client
                        .doGet(checkNicknameDuplicateUrl, null, mapOf("nickname" to "1"))
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.data.isDuplicate")
                        .isEqualTo(false)
                }
            }
            context("존재하는 닉네임을 입력하면") {
                it("실패한다.") {
                    client
                        .doGet(checkNicknameDuplicateUrl, null, mapOf("nickname" to signUpNickname))
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.data.isDuplicate")
                        .isEqualTo(true)
                }
            }
        }
    }

    private fun given(client: WebTestClient): String {
        val nickname = "nicknameTest"
        val signUpUserRequest =
            SignUpUserDto.SignUpUserRequest(
                Job.ACTOR,
                listOf(CategoryType.ETC),
                nickname,
                LocalDate.now(),
                Gender.IRRELEVANT,
                null,
                "010-1234-1234",
                "nicknameTest@test.com",
                SocialLoginType.APPLE,
                true,
                true,
                true,
                "test",
            )

        client
            .doPost(signUpBaseUrl, signUpUserRequest)
            .expectStatus()
            .isOk
            .expectBody()
            .consumeWith { println(it) }
            .jsonPath("$.result")
            .isEqualTo("SUCCESS")
        return nickname
    }
}
