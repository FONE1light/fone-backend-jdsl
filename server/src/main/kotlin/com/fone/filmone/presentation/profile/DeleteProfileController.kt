package com.fone.filmone.presentation.profile

import com.fone.filmone.application.profile.DeleteProfileFacade
import com.fone.filmone.common.response.CommonResponse
import io.swagger.annotations.Api
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@Api(tags = ["04. Profile Info"], description = "프로필 서비스")
@RestController
@RequestMapping("/api/v1/profiles")
class DeleteProfileController(
    private val deleteProfileFacade: DeleteProfileFacade,
) {

    @DeleteMapping("/{profileId}")
    @PreAuthorize("hasRole('USER')")
    suspend fun deleteProfile(
        principal: Principal,
        @PathVariable profileId: Long,
    ): CommonResponse<Unit> {
        val response = deleteProfileFacade.deleteProfile(principal.name, profileId)

        return CommonResponse.success(response)
    }
}