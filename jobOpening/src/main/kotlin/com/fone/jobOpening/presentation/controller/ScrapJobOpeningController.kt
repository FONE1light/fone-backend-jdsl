package com.fone.jobOpening.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.jobOpening.application.ScrapJobOpeningFacade
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@Api(tags = ["03. Job Opening Info"], description = "구인구직 모집 서비스")
@RestController
@RequestMapping("/api/v1/job-openings")
class ScrapJobOpeningController(
    private val scrapJobOpeningFacade: ScrapJobOpeningFacade,
) {

    @PostMapping("/{jobOpeningId}/scrap")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "구인구직 스크랩 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
    )
    suspend fun scrapJobOpening(
        principal: Principal,
        @PathVariable jobOpeningId: Long,
    ): CommonResponse<Unit> {
        val response = scrapJobOpeningFacade.scrapJobOpening(principal.name, jobOpeningId)

        return CommonResponse.success(response)
    }
}