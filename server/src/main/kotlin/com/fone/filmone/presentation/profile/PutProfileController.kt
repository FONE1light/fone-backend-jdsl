package com.fone.filmone.presentation.profile

import com.fone.filmone.application.profile.PutProfileFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.presentation.profile.RegisterProfileDto.*
import io.swagger.annotations.Api
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
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
    suspend fun putProfile(
        principal: Principal,
        @Valid @RequestBody request: RegisterProfileRequest,
        @PathVariable profileId: Long,
    ): CommonResponse<RegisterProfileResponse> {
        val response = putProfileFacade.putProfile(request, principal.name, profileId)

        return CommonResponse.success(response)
    }
}