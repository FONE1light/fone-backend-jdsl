package com.fone.common

import com.fone.common.entity.Career
import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.common.entity.Gender
import com.fone.common.entity.Type
import com.fone.common.response.CommonResponse
import com.fone.profile.presentation.dto.RegisterProfileDto
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate

object CommonProfileCallApi {
    private val registerUrl = "/api/v1/profiles"

    fun register(client: WebTestClient, accessToken: String): Long {
        val registerProfileActorRequest = RegisterProfileDto.RegisterProfileRequest(
            name = "테스트 이름",
            hookingComment = "테스트 후킹 멘트",
            birthday = LocalDate.now(),
            gender = Gender.IRRELEVANT,
            height = 180,
            weight = 70,
            email = "test12345@test.com",
            sns = "test sns",
            specialty = "test",
            details = "test",
            career = Career.IRRELEVANT,
            categories = listOf(CategoryType.ETC),
            type = Type.ACTOR,
            domains = listOf(DomainType.PAINTING),
            profileUrls = listOf("test profile url"),
            profileUrl = "test profile url"
        )

        val profile =
            (
                client.doPost(registerUrl, registerProfileActorRequest, accessToken).expectStatus().isOk.expectBody(
                    CommonResponse::class.java
                ).returnResult().responseBody?.data as LinkedHashMap<*, *>
                )["profile"]

        val profileId = (profile as LinkedHashMap<*, *>)["id"]

        return profileId.toString().toLong()
    }
}
