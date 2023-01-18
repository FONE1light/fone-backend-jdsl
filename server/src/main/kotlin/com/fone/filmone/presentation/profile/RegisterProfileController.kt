package com.fone.filmone.presentation.profile

import com.fone.filmone.application.profile.RegisterProfileFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.presentation.profile.RegisterProfileDto.RegisterProfileRequest
import com.fone.filmone.presentation.profile.RegisterProfileDto.RegisterProfileResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/profiles")
class RegisterProfileController(
    val registerProfileFacade: RegisterProfileFacade,
) {

    @PostMapping
    suspend fun registerProfile(
        principal: Principal,
        @Valid @RequestBody request: RegisterProfileRequest):
            CommonResponse<RegisterProfileResponse> {
        val response = registerProfileFacade.registerProfile(request, principal.name)
        return CommonResponse.success(response)
    }
}