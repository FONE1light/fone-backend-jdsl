package com.fone.jobOpening.presentation.dto.common

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class LocationDtoTest : DescribeSpec({
    describe("LocationDto") {
        context("RetrieveRegionsResponse") {
            it("should correctly store regions") {
                val regions = listOf("서울특별시", "부산광역시")
                val response = LocationDto.RetrieveRegionsResponse(regions)

                response.regions shouldBe regions
            }
        }

        context("RetrieveDistrictsResponse") {
            it("should correctly store districts") {
                val districts = listOf("강남구", "서초구")
                val response = LocationDto.RetrieveDistrictsResponse(districts)

                response.districts shouldBe districts
            }
        }
    }
})
