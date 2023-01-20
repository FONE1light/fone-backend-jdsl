package com.fone.filmone.presentation.profile

import com.fone.filmone.application.profile.RetrieveProfileFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.presentation.profile.RetrieveProfileDto.RetrieveProfileResponse
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/profiles")
class RetrieveProfileController(
    private val retrieveProfileFacade: RetrieveProfileFacade,
) {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    suspend fun retrieveProfile(pageable: Pageable): CommonResponse<RetrieveProfileResponse> {
        val response = retrieveProfileFacade.retrieveProfile(pageable)

        return CommonResponse.success(response)
    }
}