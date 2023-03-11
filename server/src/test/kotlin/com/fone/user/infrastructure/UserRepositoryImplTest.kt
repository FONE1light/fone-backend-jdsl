package com.fone.user.infrastructure

import com.fone.common.IntegrationTest
import com.fone.user.domain.entity.User
import com.fone.user.domain.enum.SocialLoginType
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

@IntegrationTest
class UserRepositoryImplTest(
    private val userRepositoryImpl: UserRepositoryImpl,
) : DescribeSpec({

    describe("findByEmailAndSocialLoginType") {
        val user = User(email = "test12345@gmail.com", socialLoginType = SocialLoginType.KAKAO)
        val users =
            listOf(
                user
            )
        users.forEach { userRepositoryImpl.save(it) }

        context("검색 조건에 맞는 유저가 있다면") {
            it("유저가 조회 된다.") {
                val result = userRepositoryImpl
                    .findByEmailAndSocialLoginType("test12345@gmail.com", SocialLoginType.KAKAO)

                result shouldBe user
            }
        }

        context("검색 조건에 맞는 유저가 없다면") {
            it("유저가 조회 되지 않는다 - email") {
                val result = userRepositoryImpl
                    .findByEmailAndSocialLoginType("test12@gmail.com", SocialLoginType.KAKAO)

                result shouldBe null
            }

            it("유저가 조회 되지 않는다 - socialLoginType") {
                val result = userRepositoryImpl
                    .findByEmailAndSocialLoginType("test12345@gmail.com", SocialLoginType.APPLE)

                result shouldBe null
            }

            it("유저가 조회 되지 않는다 - email & socialLoginType") {
                val result = userRepositoryImpl
                    .findByEmailAndSocialLoginType("test123@gmail.com", SocialLoginType.APPLE)

                result shouldBe null
            }
        }
    }

    describe("findByNicknameOrEmail") { }

    describe("save") { }
})
