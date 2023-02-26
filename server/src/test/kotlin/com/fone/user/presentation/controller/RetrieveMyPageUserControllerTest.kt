package com.fone.user.presentation.controller

import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.doPost
import com.fone.common.entity.CategoryType
import com.fone.common.entity.Gender
import com.fone.common.response.CommonResponse
import com.fone.user.domain.enum.Job
import com.fone.user.domain.enum.SocialLoginType
import com.fone.user.presentation.dto.SignInUserDto
import com.fone.user.presentation.dto.SignInUserDto.*
import com.fone.user.presentation.dto.SignUpUserDto
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.security.core.context.SecurityContext
import org.springframework.test.web.reactive.server.FluxExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult
import reactor.core.publisher.Flux
import java.security.Permission
import java.time.LocalDate

@IntegrationTest
class RetrieveMyPageUserControllerTest(client: WebTestClient) : CustomDescribeSpec() {

 private val signInBaseUrl = "/api/v1/users/sign-in"
 private val signUpBaseUrl = "/api/v1/users/sign-up"

 init {
  val signUpUserRequest =
   SignUpUserDto.SignUpUserRequest(
    Job.ACTOR,
    listOf(CategoryType.ETC),
    "test6",
    LocalDate.now(),
    Gender.IRRELEVANT,
    null,
    "010-1234-1234",
    "test7@test.com",
    SocialLoginType.APPLE,
    true,
    true,
    true,
    "test",
   )

  val signInUserSuccessRequest =
   SignInUserRequest(
    SocialLoginType.APPLE,
    "test7@test.com",
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

  val token = (client
   .doPost(signInBaseUrl, signInUserSuccessRequest)
   .expectStatus().isOk()
   .expectBody(CommonResponse::class.java)
   .returnResult().responseBody?.data as LinkedHashMap<*,*>)["token"]

  val accessToken = (token as LinkedHashMap<*,*>)["accessToken"]

  describe("#retrieveMyPage") {
   context("test") {
    it("test") {
     println("test..123" + accessToken)
    }
   }
  }
 }
}
