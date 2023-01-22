package com.fone.filmone.presentation.profile

import com.fone.filmone.application.profile.RetrieveProfileMyRegistrationFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.presentation.profile.RetrieveProfileMyRegistrationDto.RetrieveProfileMyRegistrationResponse
import io.swagger.annotations.Api
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@Api(tags = ["04. Profile Info"], description = "프로필 서비스")
@RestController
@RequestMapping("/api/v1/profiles")
class RetrieveProfileMyRegistrationController(
    private val retrieveProfileMyRegistrationFacade: RetrieveProfileMyRegistrationFacade,
) {

    @GetMapping("/my-registrations")
    @PreAuthorize("hasRole('USER')")
    suspend fun retrieveProfileMyRegistration(
        principal: Principal,
    ): CommonResponse<RetrieveProfileMyRegistrationResponse> {
        val response =
            retrieveProfileMyRegistrationFacade.retrieveProfileMyRegistration(principal.name)

        return CommonResponse.success(response)
    }
}