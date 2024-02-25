package com.fone.jobOpening.presentation.controller

import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPost
import com.fone.common.entity.Career
import com.fone.common.entity.CategoryType
import com.fone.common.entity.ContactMethod
import com.fone.common.entity.DomainType
import com.fone.common.entity.Gender
import com.fone.common.entity.Genre
import com.fone.common.entity.Salary
import com.fone.common.entity.Weekday
import com.fone.jobOpening.presentation.dto.FifthPage
import com.fone.jobOpening.presentation.dto.FirstPage
import com.fone.jobOpening.presentation.dto.FourthPage
import com.fone.jobOpening.presentation.dto.SecondPage
import com.fone.jobOpening.presentation.dto.SeventhPage
import com.fone.jobOpening.presentation.dto.SixthPage
import com.fone.jobOpening.presentation.dto.ThirdPage
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate

@IntegrationTest
class ValidateJobOpeningControllerTest(client: WebTestClient) : CustomDescribeSpec() {
    init {
        val url = "/api/v1/job-openings/validate"
        val (accessToken, _) = CommonUserCallApi.getAccessToken(client)
        describe("#JobOpening 검증 API") {
            context("contact 페이지") {
                it("EMAIL 케이스 성공한다") {
                    val request = FirstPage(
                        contactMethod = ContactMethod.EMAIL,
                        contact = "test@test.com"
                    )
                    client.doPost("$url/contact", request, accessToken)
                        .expectStatus().isOk.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("SUCCESS")
                }

                it("KAKAO 케이스 성공한다") {
                    val request = FirstPage(
                        contactMethod = ContactMethod.KAKAO,
                        contact = "https://open.kakao.com/test"
                    )
                    client.doPost("$url/contact", request, accessToken)
                        .expectStatus().isOk.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("SUCCESS")
                }

                it("FORM 케이스 성공한다") {
                    val request = FirstPage(
                        contactMethod = ContactMethod.GOOGLE_FORM,
                        contact = "https://docs.google.com/forms/test"
                    )
                    client.doPost("$url/contact", request, accessToken)
                        .expectStatus().isOk.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("SUCCESS")
                }

                it("이메일 케이스 실패한다") {
                    val request = FirstPage(
                        contactMethod = ContactMethod.EMAIL,
                        contact = "test"
                    )
                    client.doPost("$url/contact", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }

                it("카카오 케이스 실패한다") {
                    val request = FirstPage(
                        contactMethod = ContactMethod.KAKAO,
                        contact = "https://open.kakao.co"
                    )
                    client.doPost("$url/contact", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }

                it("구글폼 케이스 실패한다") {
                    val request = FirstPage(
                        contactMethod = ContactMethod.GOOGLE_FORM,
                        contact = "https://docs.google.com/form"
                    )
                    client.doPost("$url/contact", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
            }
            context("title 페이지") {
                it("성공한다") {
                    val request = SecondPage(
                        "제목",
                        listOf(CategoryType.ETC),
                        LocalDate.now(),
                        LocalDate.now(),
                        listOf(""),
                        ""
                    )
                    client.doPost("$url/title", request, accessToken)
                        .expectStatus().isOk.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("SUCCESS")
                }
                it("타이틀 케이스 실패한다") {
                    val request = SecondPage(
                        "",
                        listOf(CategoryType.ETC),
                        LocalDate.now(),
                        LocalDate.now(),
                        listOf(""),
                        ""
                    )
                    client.doPost("$url/title", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
                it("카테고리 케이스 실패한다") {
                    val request = SecondPage(
                        "제목",
                        listOf(),
                        LocalDate.now(),
                        LocalDate.now(),
                        listOf(""),
                        ""
                    )
                    client.doPost("$url/title", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
                it("모집기간 마감일 시작일 유효성 실패한다") {
                    val request = SecondPage(
                        "제목",
                        listOf(CategoryType.ETC),
                        LocalDate.now(),
                        LocalDate.now().minusDays(1),
                        listOf(""),
                        ""
                    )
                    client.doPost("$url/title", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
                it("모집기간 시작일 유효성 실패한다") {
                    val request = SecondPage(
                        "제목",
                        listOf(CategoryType.ETC),
                        LocalDate.now().minusDays(1),
                        LocalDate.now().minusDays(1),
                        listOf(""),
                        ""
                    )
                    client.doPost("$url/title", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
            }
            context("role 페이지") {
                it("성공한다") {
                    val request = ThirdPage(
                        casting = "모집배역",
                        domains = listOf(DomainType.ETC),
                        numberOfRecruits = 1,
                        careers = listOf(Career.NEWCOMER),
                        ageMin = 0,
                        ageMax = 100,
                        gender = Gender.MAN
                    )
                    client.doPost("$url/role", request, accessToken)
                        .expectStatus().isOk.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("SUCCESS")
                }
                it("모집배역 케이스 실패한다") {
                    val request = ThirdPage(
                        casting = "",
                        domains = listOf(DomainType.ETC),
                        numberOfRecruits = 1,
                        careers = listOf(Career.NEWCOMER),
                        ageMin = 0,
                        ageMax = 100,
                        gender = Gender.MAN
                    )
                    client.doPost("$url/role", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
                it("모집분야 케이스 실패한다") {
                    val request = ThirdPage(
                        casting = "모집배역",
                        domains = listOf(),
                        numberOfRecruits = 1,
                        careers = listOf(Career.NEWCOMER),
                        ageMin = 0,
                        ageMax = 100,
                        gender = Gender.MAN
                    )
                    client.doPost("$url/role", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
                it("모집인원 케이스 실패한다") {
                    val request = ThirdPage(
                        casting = "모집배역",
                        domains = listOf(DomainType.ETC),
                        numberOfRecruits = -1,
                        careers = listOf(Career.NEWCOMER),
                        ageMin = 0,
                        ageMax = 100,
                        gender = Gender.MAN
                    )
                    client.doPost("$url/role", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
                it("경력 케이스 실패한다") {
                    val request = ThirdPage(
                        casting = "모집배역",
                        domains = listOf(DomainType.ETC),
                        numberOfRecruits = 1,
                        careers = listOf(),
                        ageMin = 0,
                        ageMax = 100,
                        gender = Gender.MAN
                    )
                    client.doPost("$url/role", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
                it("나이 케이스 실패한다1") {
                    val request = ThirdPage(
                        casting = "모집배역",
                        domains = listOf(DomainType.ETC),
                        numberOfRecruits = 1,
                        careers = listOf(Career.NEWCOMER),
                        ageMin = 100,
                        ageMax = 0,
                        gender = Gender.MAN
                    )
                    client.doPost("$url/role", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
                it("나이 케이스 실패한다2") {
                    val request = ThirdPage(
                        casting = "모집배역",
                        domains = listOf(DomainType.ETC),
                        numberOfRecruits = 1,
                        careers = listOf(Career.NEWCOMER),
                        ageMin = -1,
                        ageMax = -1,
                        gender = Gender.MAN
                    )
                    client.doPost("$url/role", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
                it("나이 케이스 실패한다3") {
                    val request = ThirdPage(
                        casting = "모집배역",
                        domains = listOf(DomainType.ETC),
                        numberOfRecruits = 1,
                        careers = listOf(Career.NEWCOMER),
                        ageMin = 300,
                        ageMax = 300,
                        gender = Gender.MAN
                    )
                    client.doPost("$url/role", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
            }
            context("project 페이지") {
                it("성공한다") {
                    val request = FourthPage(
                        produce = "제작",
                        workTitle = "작품 제목",
                        director = "이하은",
                        genres = setOf(Genre.ACTION),
                        logline = "로그라인"
                    )
                    client.doPost("$url/project", request, accessToken)
                        .expectStatus().isOk.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("SUCCESS")
                }
                it("제작 케이스 실패한다") {
                    val request = FourthPage(
                        produce = "",
                        workTitle = "작품 제목",
                        director = "이하은",
                        genres = setOf(Genre.ACTION),
                        logline = "로그라인"
                    )
                    client.doPost("$url/project", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
                it("작품 제목 케이스 실패한다") {
                    val request = FourthPage(
                        produce = "제작",
                        workTitle = "",
                        director = "이하은",
                        genres = setOf(Genre.ACTION),
                        logline = "로그라인"
                    )
                    client.doPost("$url/project", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
                it("연출자 케이스 실패한다") {
                    val request = FourthPage(
                        produce = "제작",
                        workTitle = "작품 제목",
                        director = "",
                        genres = setOf(Genre.ACTION),
                        logline = "로그라인"
                    )
                    client.doPost("$url/project", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
                it("장르 케이스 실패한다") {
                    val request = FourthPage(
                        produce = "제작",
                        workTitle = "작품 제목",
                        director = "이하은",
                        genres = setOf(),
                        logline = "로그라인"
                    )
                    client.doPost("$url/project", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
            }
            context("ProjectDetails 페이지") {
                it("성공한다") {
                    val request = FifthPage(
                        "서울특별시",
                        "강남구",
                        null,
                        null,
                        setOf(Weekday.MON),
                        null,
                        null,
                        Salary.HOURLY,
                        -2
                    )
                    client.doPost("$url/project-details", request, accessToken)
                        .expectStatus().isOk.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("SUCCESS")
                }
                it("촬영위치 케이스 실패한다") {
                    val request = FifthPage(
                        "",
                        "강남구",
                        null,
                        null,
                        setOf(Weekday.MON),
                        null,
                        null,
                        Salary.HOURLY,
                        -2
                    )
                    client.doPost("$url/project-details", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
                it("촬영위치 케이스 실패한다") {
                    val request = FifthPage(
                        "서울특별시",
                        "",
                        null,
                        null,
                        setOf(Weekday.MON),
                        null,
                        null,
                        Salary.HOURLY,
                        -2
                    )
                    client.doPost("$url/project-details", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
                it("근무시간 endDate 케이스 실패한다") {
                    val request = FifthPage(
                        "서울특별시",
                        "강남구",
                        LocalDate.now(),
                        null,
                        setOf(Weekday.MON),
                        null,
                        null,
                        Salary.HOURLY,
                        -2
                    )
                    client.doPost("$url/project-details", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
                it("근무시간 startDate 케이스 실패한다") {
                    val request = FifthPage(
                        "서울특별시",
                        "강남구",
                        null,
                        LocalDate.now(),
                        setOf(Weekday.MON),
                        null,
                        null,
                        Salary.HOURLY,
                        -2
                    )
                    client.doPost("$url/project-details", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
            }
            context("Summary 페이지") {
                it("성공한다") {
                    val request = SixthPage(
                        "상세 요강입니다."
                    )
                    client.doPost("$url/summary", request, accessToken)
                        .expectStatus().isOk.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("SUCCESS")
                }
                it("상세 요강 케이스 실패한다") {
                    val request = SixthPage(
                        "상세"
                    )
                    client.doPost("$url/summary", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
            }
            context("Manager 페이지") {
                it("성공한다") {
                    val request = SeventhPage(
                        "이하은",
                        "test@test.com"
                    )
                    client.doPost("$url/manager", request, accessToken)
                        .expectStatus().isOk.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("SUCCESS")
                }
                it("담당자 케이스 실패한다") {
                    val request = SeventhPage(
                        "",
                        "test@test.com"
                    )
                    client.doPost("$url/manager", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
                it("이메일 케이스 실패한다") {
                    val request = SeventhPage(
                        "이하은",
                        "test"
                    )
                    client.doPost("$url/manager", request, accessToken).expectStatus().isBadRequest.expectBody()
                        .consumeWith { println(it) }.jsonPath("$.result").isEqualTo("FAIL")
                }
            }
        }
    }
}
