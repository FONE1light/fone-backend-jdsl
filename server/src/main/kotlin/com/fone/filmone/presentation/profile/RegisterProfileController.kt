package com.fone.filmone.presentation.profile

import com.fone.filmone.application.profile.RegisterProfileFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.presentation.profile.RegisterProfileDto.RegisterProfileRequest
import com.fone.filmone.presentation.profile.RegisterProfileDto.RegisterProfileResponse
import io.swagger.annotations.Api
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.validation.Valid

@Api(tags = ["04. Profile Info"], description = "프로필 서비스")
@RestController
@RequestMapping("/api/v1/profiles")
class RegisterProfileController(
    val registerProfileFacade: RegisterProfileFacade,
) {

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    suspend fun registerProfile(
        principal: Principal,
        @Valid @RequestBody request: RegisterProfileRequest,
    ):
            CommonResponse<RegisterProfileResponse> {
        val response = registerProfileFacade.registerProfile(request, principal.name)
        return CommonResponse.success(response)
    }
}