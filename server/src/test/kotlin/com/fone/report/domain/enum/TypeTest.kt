package com.fone.report.domain.enum

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class TypeTest : DescribeSpec({

    describe("invoke") {
        it("JOB_OPENING") {
            val result = Type.invoke("JOB_OPENING")
            result shouldBe Type.JOB_OPENING
        }

        it("PROFILE") {
            val result = Type.invoke("PROFILE")
            result shouldBe Type.PROFILE
        }

        it("CHATTING") {
            val result = Type.invoke("CHATTING")
            result shouldBe Type.CHATTING
        }
    }
})
