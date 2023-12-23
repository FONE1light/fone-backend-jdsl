package com.fone.jobOpening.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.jobOpening.application.LocationFacade
import com.fone.jobOpening.presentation.dto.LocationDto.RetrieveDistrictsResponse
import com.fone.jobOpening.presentation.dto.LocationDto.RetrieveRegionsResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(tags = ["03. Job Opening Info"], description = "지역 조회")
@RestController
@RequestMapping("/api/v1/job-openings/locations")
class LocationController(
    private val locationFacade: LocationFacade,
) {
    @GetMapping("/regions")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "지역구 조회")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = RetrieveRegionsResponse::class))]
    )
    suspend fun retrieveRegions(): CommonResponse<RetrieveRegionsResponse> {
        val response = locationFacade.retrieveRegions()
        return CommonResponse.success(response)
    }

    @GetMapping("/districts/{region}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "시군구 조회")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = RetrieveDistrictsResponse::class))]
    )
    suspend fun retrieveDistricts(
        @PathVariable
        region: String,
    ): CommonResponse<RetrieveDistrictsResponse> {
        val response = locationFacade.retrieveDistricts(region)
        return CommonResponse.success(response)
    }
}
