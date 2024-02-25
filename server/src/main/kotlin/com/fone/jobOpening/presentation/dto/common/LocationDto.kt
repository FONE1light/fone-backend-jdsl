package com.fone.jobOpening.presentation.dto.common

import io.swagger.v3.oas.annotations.media.Schema

class LocationDto {
    data class RetrieveRegionsResponse(
        @Schema(description = "지역", example = "[\"서울특별시\"]")
        val regions: List<String>,
    )

    data class RetrieveDistrictsResponse(
        @Schema(description = "시군구", example = "[\"강남구\"]")
        val districts: List<String>,
    )
}
