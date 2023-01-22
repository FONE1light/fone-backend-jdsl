package com.fone.filmone.presentation.competition

import com.fone.filmone.application.competition.RetrieveCompetitionFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.presentation.competition.RetrieveCompetitionDto.RetrieveCompetitionResponse
import com.fone.filmone.presentation.competition.RetrieveCompetitionDto.RetrieveCompetitionsResponse
import io.swagger.annotations.Api
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
    suspend fun retrieveCompetitions(
        principal: Principal,
        pageable: Pageable,
    ): CommonResponse<RetrieveCompetitionsResponse> {
        val response = retrieveCompetitionFacade.retrieveCompetitions(principal.name, pageable)
        return CommonResponse.success(response)
    }

    @GetMapping("/{competitionId}")
    @PreAuthorize("hasRole('USER')")
    suspend fun retrieveCompetition(
        principal: Principal,
        @PathVariable competitionId: Long,
    ): CommonResponse<RetrieveCompetitionResponse> {
        val response = retrieveCompetitionFacade.retrieveCompetition(principal.name, competitionId)
        return CommonResponse.success(response)
    }
}