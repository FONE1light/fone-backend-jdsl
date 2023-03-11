package com.fone.user.domain.entity

import com.fone.common.entity.Gender
import com.fone.common.jwt.Role
import com.fone.user.domain.enum.Job
import com.fone.user.domain.enum.SocialLoginType
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.security.core.authority.SimpleGrantedAuthority

class UserTest : DescribeSpec({

    describe("getAuthorities") {
        context("유저 권한이 주어지면") {
            it("유저 권한을 리터한다") {
                val roles = listOf(Role.ROLE_USER.toString())
                val user = User(
                    job = Job.ACTOR,
                    interests = listOf(""),
                    nickname = "",
                    birthday = null,
                    gender = Gender.IRRELEVANT,
                    profileUrl = "",
                    phoneNumber = "",
                    email = "",
                    socialLoginType = SocialLoginType.APPLE,
                    agreeToTermsOfServiceTermsOfUse = false,
                    agreeToPersonalInformation = false,
                    isReceiveMarketing = false,
                    roles = roles,
                    enabled = true
                )

                val result = user.authorities
                result shouldBe listOf(SimpleGrantedAuthority(Role.ROLE_USER.toString()))
            }
        }
    }

    describe("getPassword") {
        it("성공") {
            val user = User(
                job = Job.ACTOR,
                interests = listOf(""),
                nickname = "",
                birthday = null,
                gender = Gender.IRRELEVANT,
                profileUrl = "",
                phoneNumber = "",
                email = "",
                socialLoginType = SocialLoginType.APPLE,
                agreeToTermsOfServiceTermsOfUse = false,
                agreeToPersonalInformation = false,
                isReceiveMarketing = false,
                roles = listOf(),
                enabled = true
            )

            user.password shouldBe ""
        }
    }

    describe("getUsername") {
        it("이메일 조회 성공") {
            val user = User(
                job = Job.ACTOR,
                interests = listOf(""),
                nickname = "",
                birthday = null,
                gender = Gender.IRRELEVANT,
                profileUrl = "",
                phoneNumber = "",
                email = "test@test.com",
                socialLoginType = SocialLoginType.APPLE,
                agreeToTermsOfServiceTermsOfUse = false,
                agreeToPersonalInformation = false,
                isReceiveMarketing = false,
                roles = listOf(),
                enabled = true
            )

            user.username shouldBe "test@test.com"
        }
    }

    describe("isAccountNonExpired") {
        it("성공") {
            val user = User(
                job = Job.ACTOR,
                interests = listOf(""),
                nickname = "",
                birthday = null,
                gender = Gender.IRRELEVANT,
                profileUrl = "",
                phoneNumber = "",
                email = "test@test.com",
                socialLoginType = SocialLoginType.APPLE,
                agreeToTermsOfServiceTermsOfUse = false,
                agreeToPersonalInformation = false,
                isReceiveMarketing = false,
                roles = listOf(),
                enabled = true
            )

            user.isAccountNonExpired shouldBe false
        }
    }

    describe("isAccountNonLocked") {
        it("성공") {
            val user = User(
                job = Job.ACTOR,
                interests = listOf(""),
                nickname = "",
                birthday = null,
                gender = Gender.IRRELEVANT,
                profileUrl = "",
                phoneNumber = "",
                email = "test@test.com",
                socialLoginType = SocialLoginType.APPLE,
                agreeToTermsOfServiceTermsOfUse = false,
                agreeToPersonalInformation = false,
                isReceiveMarketing = false,
                roles = listOf(),
                enabled = true
            )

            user.isAccountNonLocked shouldBe false
        }
    }

    describe("isCredentialsNonExpired") {
        it("성공") {
            val user = User(
                job = Job.ACTOR,
                interests = listOf(""),
                nickname = "",
                birthday = null,
                gender = Gender.IRRELEVANT,
                profileUrl = "",
                phoneNumber = "",
                email = "test@test.com",
                socialLoginType = SocialLoginType.APPLE,
                agreeToTermsOfServiceTermsOfUse = false,
                agreeToPersonalInformation = false,
                isReceiveMarketing = false,
                roles = listOf(),
                enabled = true
            )

            user.isCredentialsNonExpired shouldBe false
        }
    }

    describe("isEnabled") {
        it("활성화 조회 성공") {
            val enabled = true
            val user = User(
                job = Job.ACTOR,
                interests = listOf(""),
                nickname = "",
                birthday = null,
                gender = Gender.IRRELEVANT,
                profileUrl = "",
                phoneNumber = "",
                email = "test@test.com",
                socialLoginType = SocialLoginType.APPLE,
                agreeToTermsOfServiceTermsOfUse = false,
                agreeToPersonalInformation = false,
                isReceiveMarketing = false,
                roles = listOf(),
                enabled = enabled
            )

            user.isEnabled shouldBe enabled
        }
    }
})
