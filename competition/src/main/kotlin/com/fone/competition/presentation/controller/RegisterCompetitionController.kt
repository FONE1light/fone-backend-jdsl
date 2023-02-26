package com.fone.competition.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.competition.application.RegisterCompetitionFacade
import com.fone.competition.presentation.dto.RegisterCompetitionDto.RegisterCompetitionRequest
import com.fone.competition.presentation.dto.RegisterCompetitionDto.RegisterCompetitionResponse
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

@Api(tags = ["05. Competition Info"], description = "공모전 서비스")
@RestController
@RequestMapping("/api/v1/competitions")
class RegisterCompetitionController(
    private val registerCompetitionFacade: RegisterCompetitionFacade,
) {

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "공모전 등록 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
    )
    suspend fun registerCompetition(
        principal: Principal,
        @Valid @RequestBody request: RegisterCompetitionRequest,
    ): CommonResponse<RegisterCompetitionResponse> {
        val response = registerCompetitionFacade.registerCompetition(request, principal.name)
        return CommonResponse.success(response)
    }
}
