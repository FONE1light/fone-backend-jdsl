package com.fone.profile.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.profile.application.DeleteProfileFacade
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import java.security.Principal
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(tags = ["04. Profile Info"], description = "프로필 서비스")
@RestController
@RequestMapping("/api/v1/profiles")
class DeleteProfileController(
    private val deleteProfileFacade: DeleteProfileFacade,
) {

    @DeleteMapping("/{profileId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "프로필 삭제 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
    )
    suspend fun deleteProfile(
        principal: Principal,
        @PathVariable profileId: Long,
    ): CommonResponse<Unit> {
        val response = deleteProfileFacade.deleteProfile(principal.name, profileId)

        return CommonResponse.success(response)
    }
}
