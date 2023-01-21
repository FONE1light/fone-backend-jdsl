package com.fone.filmone.presentation.profile

import com.fone.filmone.application.profile.WantProfileFacade
import com.fone.filmone.common.response.CommonResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/v1/profiles")
class WantProfileController(
    private val wantProfileFacade: WantProfileFacade,
) {

    @PatchMapping("/{profileId}/want")
    @PreAuthorize("hasRole('USER')")
    suspend fun wantProfile(
        principal: Principal,
        @PathVariable profileId: Long,
    ): CommonResponse<Unit> {
        val response = wantProfileFacade.wantProfile(principal.name, profileId)

        return CommonResponse.success(response)
    }
}