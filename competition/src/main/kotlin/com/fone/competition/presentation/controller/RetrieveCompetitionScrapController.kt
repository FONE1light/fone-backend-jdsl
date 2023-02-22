package com.fone.competition.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.competition.application.RetrieveCompetitionScrapFacade
import com.fone.competition.presentation.dto.RetrieveCompetitionScrapDto.RetrieveCompetitionScrapResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@Api(tags = ["05. Competition Info"], description = "공모전 서비스")
@RestController
@RequestMapping("/api/v1/competitions")
class RetrieveCompetitionScrapController(
    private val retrieveCompetitionScrapFacade: RetrieveCompetitionScrapFacade,
) {

    @GetMapping("/scraps")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "스크랩한 공모전 조회 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
    )
    suspend fun retrieveCompetitionScraps(
        pageable: Pageable,
        principal: Principal,
    ): CommonResponse<RetrieveCompetitionScrapResponse> {
        val response = retrieveCompetitionScrapFacade
            .retrieveCompetitionScraps(pageable, principal.name)
        return CommonResponse.success(response)
    }
}