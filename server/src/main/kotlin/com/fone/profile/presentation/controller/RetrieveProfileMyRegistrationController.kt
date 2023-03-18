package com.fone.profile.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.profile.application.RetrieveProfileMyRegistrationFacade
import com.fone.profile.presentation.dto.RetrieveProfileMyRegistrationDto.RetrieveProfileMyRegistrationResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.data.domain.Pageable
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
    @ApiOperation(value = "내가 등록한 프로필 조회 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = RetrieveProfileMyRegistrationResponse::class))]
    )
    suspend fun retrieveProfileMyRegistration(
        principal: Principal,
        pageable: Pageable,
    ): CommonResponse<RetrieveProfileMyRegistrationResponse> {
        val response = retrieveProfileMyRegistrationFacade.retrieveProfileMyRegistration(
            pageable,
            principal.name
        )

        return CommonResponse.success(response)
    }
}
