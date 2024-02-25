package com.fone.profile.presentation.controller

import com.fone.common.entity.Type
import com.fone.common.response.CommonResponse
import com.fone.profile.application.RetrieveProfilesFacade
import com.fone.profile.presentation.dto.RetrieveProfileResponse
import com.fone.profile.presentation.dto.RetrieveProfilesRequest
import com.fone.profile.presentation.dto.RetrieveProfilesResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@Api(tags = ["04. Profile Info"], description = "프로필 서비스")
@RestController
@RequestMapping("/api/v1/profiles")
class RetrieveProfilesController(
    private val retrieveProfilesFacade: RetrieveProfilesFacade,
) {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "프로필 리스트 조회 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = RetrieveProfilesResponse::class))]
    )
    suspend fun retrieveProfiles(
        principal: Principal,
        pageable: Pageable,
        @ModelAttribute request: RetrieveProfilesRequest,
    ): CommonResponse<RetrieveProfilesResponse> {
        val response = retrieveProfilesFacade.retrieveProfiles(pageable, principal.name, request)

        return CommonResponse.success(response)
    }

    @GetMapping("/{profileId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "프로필 디테일 조회 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = RetrieveProfileResponse::class))]
    )
    suspend fun retrieveProfile(
        principal: Principal,
        @RequestParam type: Type,
        @PathVariable profileId: Long,
    ): CommonResponse<RetrieveProfileResponse> {
        val response = retrieveProfilesFacade.retrieveProfile(principal.name, type, profileId)

        return CommonResponse.success(response)
    }
}
