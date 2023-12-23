package com.fone.jobOpening.presentation.dto.common

import com.fone.jobOpening.domain.entity.Location
import io.swagger.v3.oas.annotations.media.Schema

data class LocationDto(
    @Schema(
        description = "광역시,특별시,도",
        example = "서울특별시"
    )
    val region: String,
    @Schema(
        description = "시,군,구",
        example = "강남구"
    )
    val district: String,
)

fun Location.toDto() = LocationDto(region, district)
