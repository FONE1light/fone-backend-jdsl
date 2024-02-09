package com.fone.jobOpening.presentation.dto

import io.swagger.v3.oas.annotations.media.Schema

class LocationDto {
    data class RetrieveRegionsResponse(
        @Schema(description = "지역 (deprecated 예정)", example = "서울특별시")
        val result: List<String>,

        @Schema(description = "지역", example = "서울특별시")
        val regions: List<String>,
    )

    data class RetrieveDistrictsResponse(
        @Schema(description = "시군구 (deprecated 예정)", example = "강남구")
        val result: List<String>,

        @Schema(description = "시군구", example = "강남구")
        val district: List<String>,
    )
}
