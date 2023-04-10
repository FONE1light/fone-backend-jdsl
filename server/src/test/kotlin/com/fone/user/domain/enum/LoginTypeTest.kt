package com.fone.user.domain.enum

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class LoginTypeTest : DescribeSpec({

    describe("invoke") {
        it("KAKAO") {
            val result = LoginType.invoke("KAKAO")
            result shouldBe LoginType.KAKAO
        }

        it("NAVER") {
            val result = LoginType.invoke("NAVER")
            result shouldBe LoginType.NAVER
        }

        it("GOOGLE") {
            val result = LoginType.invoke("GOOGLE")
            result shouldBe LoginType.GOOGLE
        }

        it("APPLE") {
            val result = LoginType.invoke("APPLE")
            result shouldBe LoginType.APPLE
        }
    }
})
