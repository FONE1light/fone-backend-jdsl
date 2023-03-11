package com.fone.user.domain.enum

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class SocialLoginTypeTest : DescribeSpec({

    describe("invoke") {
        it("KAKAO") {
            val result = SocialLoginType.invoke("KAKAO")
            result shouldBe SocialLoginType.KAKAO
        }

        it("NAVER") {
            val result = SocialLoginType.invoke("NAVER")
            result shouldBe SocialLoginType.NAVER
        }

        it("GOOGLE") {
            val result = SocialLoginType.invoke("GOOGLE")
            result shouldBe SocialLoginType.GOOGLE
        }

        it("APPLE") {
            val result = SocialLoginType.invoke("APPLE")
            result shouldBe SocialLoginType.APPLE
        }
    }
})
