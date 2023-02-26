package com.fone.profile.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.profile.application.RegisterProfileFacade
import com.fone.profile.presentation.dto.RegisterProfileDto.RegisterProfileRequest
import com.fone.profile.presentation.dto.RegisterProfileDto.RegisterProfileResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import java.security.Principal
import javax.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(tags = ["04. Profile Info"], description = "프로필 서비스")
@RestController
@RequestMapping("/api/v1/profiles")
class RegisterProfileController(
    val registerProfileFacade: RegisterProfileFacade,
) {

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "프로필 등록 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
    )
    suspend fun registerProfile(
        principal: Principal,
        @Valid @RequestBody request: RegisterProfileRequest,
    ): CommonResponse<RegisterProfileResponse> {
        val response = registerProfileFacade.registerProfile(request, principal.name)
        return CommonResponse.success(response)
    }
}
