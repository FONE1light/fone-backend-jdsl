package com.fone.filmone.presentation.competition

import com.fone.filmone.application.competition.ScrapCompetitionFacade
import com.fone.filmone.common.response.CommonResponse
import io.swagger.annotations.Api
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
    private val scrapCompetitionFacade: ScrapCompetitionFacade,
) {

    @PostMapping("/{competitionId}/scrap")
    @PreAuthorize("hasRole('USER')")
    suspend fun scrapCompetition(
        principal: Principal,
        @PathVariable competitionId: Long,
    ): CommonResponse<Unit> {
        val response = scrapCompetitionFacade.scrapCompetition(principal.name, competitionId)
        return CommonResponse.success(response)
    }
}