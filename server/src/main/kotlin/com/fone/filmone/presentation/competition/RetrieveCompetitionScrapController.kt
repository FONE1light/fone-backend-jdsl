package com.fone.filmone.presentation.competition

import com.fone.filmone.application.competition.RetrieveCompetitionScrapFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.presentation.competition.RetrieveCompetitionScrapDto.RetrieveCompetitionScrapResponse
import io.swagger.annotations.Api
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
    suspend fun retrieceCompetitionScraps(
        principal: Principal,
    ): CommonResponse<RetrieveCompetitionScrapResponse> {
        val response = retrieveCompetitionScrapFacade.retrieveCompetitionScraps(principal.name)
        return CommonResponse.success(response)
    }
}