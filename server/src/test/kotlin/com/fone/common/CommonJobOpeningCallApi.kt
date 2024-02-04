package com.fone.common

import com.fone.common.entity.Career
import com.fone.common.entity.CategoryType
import com.fone.common.entity.ContactMethod
import com.fone.common.entity.DomainType
import com.fone.common.entity.Gender
import com.fone.common.entity.Genre
import com.fone.common.entity.Salary
import com.fone.common.entity.Type
import com.fone.common.entity.Weekday
import com.fone.common.response.CommonResponse
import com.fone.jobOpening.presentation.dto.RegisterJobOpeningDto
import com.fone.jobOpening.presentation.dto.ValidateJobOpeningDto
import org.springframework.test.web.reactive.server.WebTestClient

object CommonJobOpeningCallApi {
    val registerJobOpeningActorRequest =
        RegisterJobOpeningDto.RegisterJobOpeningRequest(
            firstPage = ValidateJobOpeningDto.FirstPage(
                contactMethod = ContactMethod.EMAIL,
                contact = "https://docs.google.com/forms/..."
            ),
            secondPage = ValidateJobOpeningDto.SecondPage(
                title = "title",
                categories = listOf(CategoryType.WEB_DRAMA),
                recruitmentStartDate = null,
                recruitmentEndDate = null,
                imageUrls = listOf("https://www.naver.com"),
                representativeImageUrl = "https://www.naver.com"
            ),
            thirdPage = ValidateJobOpeningDto.ThirdPage(
                casting = "casting",
                domains = listOf(DomainType.SCENARIO),
                numberOfRecruits = 1,
                gender = Gender.MAN,
                ageMax = 40,
                ageMin = 20,
                careers = listOf(Career.IRRELEVANT)
            ),
            fourthPage = ValidateJobOpeningDto.FourthPage(
                produce = "produce",
                workTitle = "workTitle",
                director = "director",
                genres = setOf(Genre.ACTION),
                logline = "logline"
            ),
            fifthPage = ValidateJobOpeningDto.FifthPage(
                workingCity = "서울특별시",
                workingDistrict = "도봉구",
                workingStartDate = null,
                workingEndDate = null,
                selectedDays = setOf(Weekday.MON),
                workingStartTime = null,
                workingEndTime = null,
                salaryType = Salary.HOURLY,
                salary = 100000
            ),
            sixthPage = ValidateJobOpeningDto.SixthPage(
                details = "details"
            ),
            seventhPage = ValidateJobOpeningDto.SeventhPage(
                manager = "manager",
                email = "email"
            ),
            type = Type.ACTOR
        )

    val registerJobOpeningStaffRequest = registerJobOpeningActorRequest.copy(
        type = Type.STAFF
    )
    private val registerUrl = "/api/v1/job-openings"

    fun register(
        client: WebTestClient,
        accessToken: String,
    ): Long {
        val jobOpening =
            (
                client.doPost(registerUrl, registerJobOpeningActorRequest, accessToken).expectStatus().isOk.expectBody(
                    CommonResponse::class.java
                ).returnResult().responseBody?.data as LinkedHashMap<*, *>
                )["jobOpening"]

        val jobOpeningId = (jobOpening as LinkedHashMap<*, *>)["id"]

        return jobOpeningId.toString().toLong()
    }
}
