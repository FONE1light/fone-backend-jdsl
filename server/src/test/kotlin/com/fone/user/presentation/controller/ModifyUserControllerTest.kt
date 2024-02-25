package com.fone.user.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPatch
import com.fone.common.entity.CategoryType
import com.fone.common.jwt.Role
import com.fone.common.response.CommonResponse
import com.fone.user.domain.enum.Job
import com.fone.user.domain.enum.LoginType
import com.fone.user.domain.repository.UserRepository
import com.fone.user.presentation.dto.AdminModifyUserRequest
import com.fone.user.presentation.dto.ModifyUserRequest
import com.fone.user.presentation.dto.ModifyUserResponse
import com.fone.user.presentation.dto.SocialSignInUserRequest
import io.kotest.matchers.shouldBe
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class ModifyUserControllerTest(
    client: WebTestClient,
    userRepository: UserRepository,
    objectMapper: ObjectMapper,
) :
    CustomDescribeSpec() {

    private val modifyUrl = "/api/v1/users"

    init {
        val (accessToken, email) = CommonUserCallApi.getAccessToken(client)
        val successModifyUserRequest =
            ModifyUserRequest(
                Job.ACTOR,
                listOf(CategoryType.ETC),
                "test1515151",
                ""
            )
        val adminModifyUserRequest =
            AdminModifyUserRequest(
                job = Job.STAFF,
                roles = listOf(Role.ROLE_USER),
                isVerified = true
            )

        describe("#modify") {
            context("존재하는 유저의 정보로 수정을 요청하면") {
                it("성공한다") {
                    client
                        .doPatch(modifyUrl, successModifyUserRequest, accessToken)
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
            context("어드민 권한이 있는 경우 isVerified를 true로 수정하면") {
                it("성공한다") {
                    val user = userRepository.findByEmail(email)!!
                    user.roles = listOf(Role.ROLE_USER, Role.ROLE_ADMIN).map { it.toString() }
                    userRepository.save(user)
                    val newAccessToken = getNewToken(client, email)
                    client
                        .doPatch(modifyUrl + "/${user.id}", adminModifyUserRequest, newAccessToken)
                        .expectStatus()
                        .isOk
                        .expectBody()
                        .consumeWith {
                            val response = objectMapper
                                .readValue<CommonResponse<ModifyUserResponse>>(it.responseBody!!)
                            response.data!!.user.isVerified shouldBe true
                        }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
            context("어드민 권한이 없는 경우, api요청은") {
                it("실패한다") {
                    val user = userRepository.findByEmail(email)!!
                    user.roles = listOf(Role.ROLE_USER).map { it.toString() }
                    userRepository.save(user)
                    val newAccessToken = getNewToken(client, email)
                    client
                        .doPatch(modifyUrl + "/${user.id}", adminModifyUserRequest, newAccessToken)
                        .expectStatus().is5xxServerError
                }
            }
        }
    }

    private fun getNewToken(client: WebTestClient, email: String): String {
        return (
            CommonUserCallApi.signIn(
                client,
                SocialSignInUserRequest(LoginType.APPLE, "token:$email")
            )["token"] as Map<*, *>
            )["accessToken"] as String
    }
}
