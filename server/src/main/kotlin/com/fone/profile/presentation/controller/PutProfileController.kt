package com.fone.profile.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.profile.application.PutProfileFacade
import com.fone.profile.presentation.dto.RegisterProfileDto.RegisterProfileRequest
import com.fone.profile.presentation.dto.RegisterProfileDto.RegisterProfileResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.validation.Valid

@Api(tags = ["04. Profile Info"], description = "프로필 서비스")
@RestController
@RequestMapping("/api/v1/profiles")
class PutProfileController(
    private val putProfileFacade: PutProfileFacade,
) {

    @PutMapping("/{profileId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "프로필 수정 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = RegisterProfileResponse::class))]
    )
    suspend fun putProfile(
        principal: Principal,
        @Valid @RequestBody
        request: RegisterProfileRequest,
        @PathVariable profileId: Long,
    ): CommonResponse<RegisterProfileResponse> {
        val response = putProfileFacade.putProfile(request, principal.name, profileId)

        return CommonResponse.success(response)
    }
}
