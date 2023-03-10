package com.fone.competition.presentation.controller

import com.fone.common.response.CommonResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@Api(tags = ["05. Competition Info"], description = "공모전 서비스")
@RestController
@RequestMapping("/api/v1/competitions")
class ScrapCompetitionController(
    private val scrapCompetitionFacade: com.fone.competition.application.ScrapCompetitionFacade,
) {

    @PostMapping("/{competitionId}/scrap")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "공모전 스크랩하기 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공"
    )
    suspend fun scrapCompetition(
        principal: Principal,
        @PathVariable competitionId: Long,
    ): CommonResponse<Unit> {
        val response = scrapCompetitionFacade.scrapCompetition(principal.name, competitionId)
        return CommonResponse.success(response)
    }
}
