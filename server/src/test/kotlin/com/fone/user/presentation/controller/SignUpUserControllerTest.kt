package com.fone.user.presentation.controller

import com.fone.common.TCIntegrationTest
import com.fone.common.entity.CategoryType
import com.fone.common.entity.Gender
import com.fone.user.domain.enum.Job
import com.fone.user.domain.enum.SocialLoginType
import com.fone.user.presentation.dto.SignUpUserDto.SignUpUserRequest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.body
import org.testcontainers.junit.jupiter.Testcontainers
import reactor.core.publisher.Mono
import java.time.LocalDate

@Testcontainers
class SignUpUserControllerTest : TCIntegrationTest() {

    @Autowired
    private lateinit var wtc: WebTestClient

    @Test
    fun `sign up user`() {
        val signUpUserRequest = SignUpUserRequest(
            Job.ACTOR,
            listOf(CategoryType.ETC),
            "test5",
            LocalDate.now(),
            Gender.IRRELEVANT,
            null,
            "010-1234-1234",
            "test5@test.com",
            SocialLoginType.APPLE,
            true,
            true,
            true,
            "test",
        )

        wtc.post().uri("/api/v1/users/sign-up")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(signUpUserRequest))
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .consumeWith { println(it) }
            .jsonPath("$.result").isNotEmpty
    }
}