package com.fone.filmone.presentation.competition

import com.fone.filmone.application.competition.RegisterCompetitionFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.presentation.competition.RegisterCompetitionDto.*
import io.swagger.annotations.Api
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.validation.Valid

@Api(tags = ["05. Competition Info"], description = "공모전 서비스")
@RestController
@RequestMapping("/api/v1/competition")
class RegisterCompetitionController(
    private val registerCompetitionFacade: RegisterCompetitionFacade,
) {

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    suspend fun registerCompetition(
        principal: Principal,
        @Valid @RequestBody request: RegisterCompetitionRequest,
    ): CommonResponse<RegisterCompetitionResponse> {
        val response = registerCompetitionFacade.registerCompetition(request, principal.name)
        return CommonResponse.success(response)
    }
}