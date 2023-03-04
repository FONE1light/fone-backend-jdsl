package com.fone.profile.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.profile.application.WantProfileFacade
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@Api(tags = ["04. Profile Info"], description = "프로필 서비스")
@RestController
@RequestMapping("/api/v1/profiles")
class WantProfileController(
    private val wantProfileFacade: WantProfileFacade
) {

    @PostMapping("/{profileId}/want")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "프로필 찜하기 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공"
    )
    suspend fun wantProfile(
        principal: Principal,
        @PathVariable profileId: Long
    ): CommonResponse<Unit> {
        val response = wantProfileFacade.wantProfile(principal.name, profileId)

        return CommonResponse.success(response)
    }
}
