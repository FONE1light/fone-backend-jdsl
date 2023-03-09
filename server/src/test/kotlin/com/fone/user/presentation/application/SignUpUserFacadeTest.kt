package com.fone.user.presentation.application

import com.fone.common.entity.CategoryType
import com.fone.common.entity.Gender
import com.fone.user.application.SignUpUserFacade
import com.fone.user.domain.enum.Job
import com.fone.user.domain.enum.SocialLoginType
import com.fone.user.domain.service.SignUpUserService
import com.fone.user.presentation.dto.SignUpUserDto.SignUpUserRequest
import com.fone.user.presentation.dto.SignUpUserDto.SignUpUserResponse
import com.fone.user.presentation.dto.common.UserDto
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import java.time.LocalDate

class SignUpUserFacadeTest : DescribeSpec({
    val signUpUserService: SignUpUserService = mockk()
    val signUpUserFacade = SignUpUserFacade(signUpUserService)

    describe("#signUpUserFacade") {
        context("given request") {
            val signUpUserRequest =
                SignUpUserRequest(
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
                    "test"
                )
            val signUpUserResponse =
                SignUpUserResponse(
                    user = UserDto(
                        id = 1,
                        Job.ACTOR,
                        listOf(CategoryType.ETC),
                        "test5",
                        LocalDate.now(),
                        Gender.IRRELEVANT,
                        "",
                        "010-1234-1234",
                        "test5@test.com",
                        SocialLoginType.APPLE,
                        true,
                        true,
                        true,
                        true
                    )
                )
            coEvery { signUpUserService.signUpUser(signUpUserRequest) } returns signUpUserResponse
            it("return response") {
                val result = signUpUserFacade.signUp(signUpUserRequest)
                result shouldBe signUpUserResponse
            }
        }
    }
})
