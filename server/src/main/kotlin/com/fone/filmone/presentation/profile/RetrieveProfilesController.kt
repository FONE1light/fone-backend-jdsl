package com.fone.filmone.presentation.profile

import com.fone.filmone.application.profile.RetrieveProfilesFacade
import com.fone.common.response.CommonResponse
import com.fone.filmone.domain.common.Type
import com.fone.filmone.presentation.profile.RetrieveProfilesDto.RetrieveProfileResponse
import com.fone.filmone.presentation.profile.RetrieveProfilesDto.RetrieveProfilesResponse
import io.swagger.annotations.Api
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

@Api(tags = ["04. Profile Info"], description = "프로필 서비스")
@RestController
@RequestMapping("/api/v1/profiles")
class RetrieveProfilesController(
    private val retrieveProfilesFacade: RetrieveProfilesFacade,
) {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    suspend fun retrieveProfiles(
        principal: Principal,
        pageable: Pageable,
        @RequestParam type: Type,
    ): CommonResponse<RetrieveProfilesResponse> {
        val response = retrieveProfilesFacade.retrieveProfiles(pageable, principal.name, type)

        return CommonResponse.success(response)
    }

    @GetMapping("/{profileId}")
    @PreAuthorize("hasRole('USER')")
    suspend fun retrieveProfile(
        principal: Principal,
        @RequestParam type: Type,
        @PathVariable profileId: Long,
    ): CommonResponse<RetrieveProfileResponse> {
        val response = retrieveProfilesFacade.retrieveProfile(principal.name, type, profileId)

        return CommonResponse.success(response)
    }
}