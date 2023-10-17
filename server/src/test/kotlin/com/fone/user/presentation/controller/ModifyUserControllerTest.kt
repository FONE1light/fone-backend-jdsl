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
import com.fone.user.presentation.dto.ModifyUserDto
import com.fone.user.presentation.dto.SignInUserDto
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
            ModifyUserDto.ModifyUserRequest(
                Job.ACTOR,
                listOf(CategoryType.ETC),
                "test1515151",
                ""
            )
        val verifiedUserRequest =
            ModifyUserDto.ModifyUserRequest(
                Job.VERIFIED,
                listOf(CategoryType.ETC),
                "test1515151",
                ""
            )
        val adminModifyUserRequest =
            ModifyUserDto.AdminModifyUserRequest(
                job = Job.VERIFIED,
                roles = listOf(Role.ROLE_USER)
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
            context("유저의 스스로 Job.VERIFIED 정보로 수정을 요청하면") {
                it("실패한다") {
                    client
                        .doPatch(modifyUrl, verifiedUserRequest, accessToken)
                        .expectStatus()
                        .isForbidden
                        .expectBody()
                        .consumeWith { println(it) }
                        .jsonPath("$.result")
                        .isEqualTo("FAIL")
                }
            }
            context("어드민 권한이 있는 경우 Role, Job 수정하면") {
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
                                .readValue<CommonResponse<ModifyUserDto.ModifyUserResponse>>(it.responseBody!!)
                            response.data!!.user.job shouldBe Job.VERIFIED
                        }
                        .jsonPath("$.result")
                        .isEqualTo("SUCCESS")
                }
            }
        }
    }

    private fun getNewToken(client: WebTestClient, email: String): String {
        return (
            CommonUserCallApi.signIn(
                client,
                SignInUserDto.SocialSignInUserRequest(LoginType.APPLE, "token:$email")
            )["token"] as Map<*, *>
            )["accessToken"] as String
    }
}
