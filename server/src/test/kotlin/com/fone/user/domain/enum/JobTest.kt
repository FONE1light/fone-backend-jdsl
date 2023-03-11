package com.fone.user.domain.enum

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class JobTest : DescribeSpec({

    describe("invoke") {
        it("ACTOR") {
            val result = Job.invoke("ACTOR")
            result shouldBe Job.ACTOR
        }

        it("STAFF") {
            val result = Job.invoke("STAFF")
            result shouldBe Job.STAFF
        }

        it("NORMAL") {
            val result = Job.invoke("NORMAL")
            result shouldBe Job.NORMAL
        }

        it("HUNTER") {
            val result = Job.invoke("HUNTER")
            result shouldBe Job.HUNTER
        }
    }
})
