package com.fone.common

import com.fone.common.entity.CategoryType
import com.fone.common.entity.Gender
import com.fone.common.response.CommonResponse
import com.fone.user.domain.enum.Job
import com.fone.user.domain.enum.SocialLoginType
import com.fone.user.presentation.dto.SignInUserDto
import com.fone.user.presentation.dto.SignUpUserDto
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate
import java.util.*
import kotlin.collections.LinkedHashMap

object CommonUserCallApi {
    private const val signInBaseUrl = "/api/v1/users/sign-in"
    private const val signUpBaseUrl = "/api/v1/users/sign-up"

    fun getAccessToken(client: WebTestClient): Pair<String, String> {
        val nickname = UUID.randomUUID().toString()
        val email = "$nickname@test.com"

        val signUpUserRequest =
            SignUpUserDto.SignUpUserRequest(
                Job.ACTOR,
                listOf(CategoryType.ETC),
                nickname,
                LocalDate.now(),
                Gender.IRRELEVANT,
                null,
                "010-1234-1234",
                email,
                SocialLoginType.APPLE,
                true,
                true,
                true,
                "test"
            )

        val signInUserSuccessRequest =
            SignInUserDto.SignInUserRequest(
                SocialLoginType.APPLE,
                email,
                "test"
            )

        client
            .doPost(signUpBaseUrl, signUpUserRequest)
            .expectStatus()
            .isOk
            .expectBody()
            .consumeWith { println(it) }
            .jsonPath("$.result")
            .isEqualTo("SUCCESS")

        val token =
            (
                client
                    .doPost(signInBaseUrl, signInUserSuccessRequest)
                    .expectStatus()
                    .isOk
                    .expectBody(CommonResponse::class.java)
                    .returnResult()
                    .responseBody
                    ?.data as LinkedHashMap<*, *>
                )["token"]

        val accessToken = (token as LinkedHashMap<*, *>)["accessToken"]

        return Pair(accessToken.toString(), email)
    }

    fun signUp(client: WebTestClient): Pair<String, String> {
        val nickname = UUID.randomUUID().toString()
        val email = "$nickname@test.com"

        val signUpUserRequest =
            SignUpUserDto.SignUpUserRequest(
                Job.ACTOR,
                listOf(CategoryType.ETC),
                nickname,
                LocalDate.now(),
                Gender.IRRELEVANT,
                null,
                "010-1234-1234",
                email,
                SocialLoginType.APPLE,
                true,
                true,
                true,
                "test"
            )

        client
            .doPost(signUpBaseUrl, signUpUserRequest)
            .expectStatus()
            .isOk
            .expectBody()
            .consumeWith { println(it) }
            .jsonPath("$.result")
            .isEqualTo("SUCCESS")

        return Pair(nickname, email)
    }
}
