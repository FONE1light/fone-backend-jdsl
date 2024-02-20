package com.fone.jobOpening.presentation.dto.common

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class JobOpeningDtoTest : DescribeSpec({

    describe("isContactable function") {
        // JobOpeningDto의 인스턴스 생성은 필요하지 않습니다.
        // 대신, isContactable 메서드에 직접 날짜를 전달합니다.

        context("when both recruitment start and end dates are null") {
            it("should return true") {
                isContactable(null, null) shouldBe true
            }
        }

        context("when current date is between recruitment start and end dates") {
            val startDate = LocalDate.now().minusDays(1)
            val endDate = LocalDate.now().plusDays(1)

            it("should return true") {
                isContactable(startDate, endDate) shouldBe true
            }
        }

        context("when current date is between recruitment start and end dates") {
            val startDate = LocalDate.now().minusDays(0)
            val endDate = LocalDate.now().plusDays(0)

            it("should return true") {
                isContactable(startDate, endDate) shouldBe true
            }
        }

        context("when current date is before the recruitment start date") {
            val startDate = LocalDate.now().plusDays(1)
            val endDate = LocalDate.now().plusDays(2)

            it("should return false") {
                isContactable(startDate, endDate) shouldBe false
            }
        }

        context("when current date is after the recruitment end date") {
            val startDate = LocalDate.now().minusDays(2)
            val endDate = LocalDate.now().minusDays(1)

            it("should return false") {
                isContactable(startDate, endDate) shouldBe false
            }
        }
    }
})
