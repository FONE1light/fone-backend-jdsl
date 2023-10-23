package com.fone.common

import com.fone.common.entity.CategoryType
import com.fone.common.entity.Gender
import com.fone.common.response.CommonResponse
import com.fone.user.domain.enum.Job
import com.fone.user.domain.enum.LoginType
import com.fone.user.presentation.dto.SignInUserDto
import com.fone.user.presentation.dto.SignUpUserDto
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate
import java.util.UUID

object CommonUserCallApi {
    private const val signInBaseUrl = "/api/v1/users/social/sign-in"
    private const val signUpBaseUrl = "/api/v1/users/social/sign-up"

    fun getAccessToken(client: WebTestClient): Pair<String, String> {
        val nickname = UUID.randomUUID().toString()
        val email = "$nickname@test.com"
        val token = getToken(client, nickname, email)["token"]
        val accessToken = (token as Map<*, *>)["accessToken"]

        return Pair(accessToken.toString(), email)
    }

    fun getToken(client: WebTestClient, nickname: String, email: String): Map<*, *> {
        val signUpUserRequest =
            SignUpUserDto.SocialSignUpUserRequest(
                Job.ACTOR,
                listOf(CategoryType.ETC),
                nickname,
                LocalDate.now(),
                Gender.IRRELEVANT,
                null,
                TestGenerator.getRandomPhoneNumber(),
                email,
                email,
                LoginType.APPLE,
                true,
                true,
                true,
                "token:$email"
            )

        val signInUserSuccessRequest =
            SignInUserDto.SocialSignInUserRequest(
                LoginType.APPLE,
                "token:$email"
            )

        client
            .doPost(signUpBaseUrl, signUpUserRequest)
            .expectStatus()
            .isOk
            .expectBody()
            .consumeWith { println(it) }
            .jsonPath("$.result")
            .isEqualTo("SUCCESS")

        return signIn(client, signInUserSuccessRequest)
    }

    fun signIn(client: WebTestClient, signInRequest: SignInUserDto.SocialSignInUserRequest): Map<*, *> {
        return client
            .doPost(signInBaseUrl, signInRequest)
            .expectStatus()
            .isOk
            .expectBody(CommonResponse::class.java)
            .returnResult()
            .responseBody
            ?.data as LinkedHashMap<*, *>
    }
    fun signUp(client: WebTestClient): Pair<String, String> {
        val nickname = UUID.randomUUID().toString().substring(0, 5)
        val email = "$nickname@test.com"

        val signUpUserRequest =
            SignUpUserDto.SocialSignUpUserRequest(
                Job.ACTOR,
                listOf(CategoryType.ETC),
                nickname,
                LocalDate.now(),
                Gender.IRRELEVANT,
                null,
                TestGenerator.getRandomPhoneNumber(),
                email,
                email,
                LoginType.APPLE,
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
