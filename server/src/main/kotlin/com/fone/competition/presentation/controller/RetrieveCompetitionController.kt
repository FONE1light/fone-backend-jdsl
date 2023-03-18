package com.fone.competition.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.competition.application.RetrieveCompetitionFacade
import com.fone.competition.presentation.dto.RetrieveCompetitionDto.RetrieveCompetitionResponse
import com.fone.competition.presentation.dto.RetrieveCompetitionDto.RetrieveCompetitionsResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@Api(tags = ["05. Competition Info"], description = "공모전 서비스")
@RestController
@RequestMapping("/api/v1/competitions")
class RetrieveCompetitionController(
    private val retrieveCompetitionFacade: RetrieveCompetitionFacade,
) {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "공모전 리스트 조회 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = RetrieveCompetitionsResponse::class))]
    )
    suspend fun retrieveCompetitions(
        principal: Principal,
        pageable: Pageable,
    ): CommonResponse<RetrieveCompetitionsResponse> {
        val response = retrieveCompetitionFacade.retrieveCompetitions(principal.name, pageable)
        return CommonResponse.success(response)
    }

    @GetMapping("/{competitionId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "공모전 디테일 조회 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = RetrieveCompetitionResponse::class))]
    )
    suspend fun retrieveCompetition(
        principal: Principal,
        @PathVariable competitionId: Long,
    ): CommonResponse<RetrieveCompetitionResponse> {
        val response = retrieveCompetitionFacade.retrieveCompetition(principal.name, competitionId)
        return CommonResponse.success(response)
    }
}
