package com.fone.common

import com.fone.common.entity.Career
import com.fone.common.entity.CategoryType
import com.fone.common.entity.ContactMethod
import com.fone.common.entity.DomainType
import com.fone.common.entity.Gender
import com.fone.common.entity.Type
import com.fone.common.response.CommonResponse
import com.fone.jobOpening.presentation.dto.ValidateJobOpeningDto
import com.fone.profile.domain.enum.SNS
import com.fone.profile.presentation.dto.RegisterProfileDto.RegisterProfileRequest
import com.fone.profile.presentation.dto.ValidateProfileDto
import com.fone.profile.presentation.dto.common.ProfileSnsUrl
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate

object CommonProfileCallApi {
    val registerProfileActorRequest = RegisterProfileRequest(
        firstPage = ValidateJobOpeningDto.FirstPage(
            contactMethod = ContactMethod.EMAIL,
            contact = "https://docs.google.com/forms/..."
        ),
        secondPage = ValidateProfileDto.SecondPage(
            name = "테스트 이름",
            hookingComment = "테스트 후킹 멘트",
            profileImages = listOf("test profile url"),
            representativeImageUrl = "test profile url"
        ),
        thirdPage = ValidateProfileDto.ThirdPage(
            birthday = LocalDate.ofYearDay(2020, 1),
            gender = Gender.MAN,
            height = 180,
            weight = 70,
            email = "test@test.com",
            specialty = "test",
            domains = null,
            snsUrls = listOf(
                ProfileSnsUrl("https://www.instagram.com/test", SNS.INSTAGRAM),
                ProfileSnsUrl("https://www.youtube.com/test", SNS.YOUTUBE)
            )
        ),
        fourthPage = ValidateProfileDto.FourthPage(
            details = "test"
        ),
        fifthPage = ValidateProfileDto.FifthPage(
            career = Career.IRRELEVANT,
            careerDetail = "test"
        ),
        sixthPage = ValidateProfileDto.SixthPage(
            categories = listOf(CategoryType.ETC)
        ),
        type = Type.ACTOR
    )

    val registerProfileStaffRequest = registerProfileActorRequest.copy(
        type = Type.STAFF,
        thirdPage = registerProfileActorRequest.thirdPage.copy(
            domains = listOf(DomainType.PAINTING)
        )
    )

    private val registerUrl = "/api/v1/profiles"

    fun register(client: WebTestClient, accessToken: String): Long {
        val snsUrls = listOf(
            ProfileSnsUrl("https://www.instagram.com/test", SNS.INSTAGRAM),
            ProfileSnsUrl("https://www.youtube.com/test", SNS.YOUTUBE)
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
