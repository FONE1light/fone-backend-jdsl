package com.fone.question.domain.enum

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class TypeTest : DescribeSpec({

    describe("invoke") {
        it("USE_QUESTION") {
            val result = Type.invoke("USE_QUESTION")
            result shouldBe Type.USE_QUESTION
        }

        it("VOICE_OF_THE_CUSTOMER") {
            val result = Type.invoke("VOICE_OF_THE_CUSTOMER")
            result shouldBe Type.VOICE_OF_THE_CUSTOMER
        }

        it("ALLIANCE") {
            val result = Type.invoke("ALLIANCE")
            result shouldBe Type.ALLIANCE
        }
    }
})
