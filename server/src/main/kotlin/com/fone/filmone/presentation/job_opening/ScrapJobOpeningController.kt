package com.fone.filmone.presentation.job_opening

import com.fone.filmone.application.job_opening.ScrapJobOpeningFacade
import com.fone.filmone.common.response.CommonResponse
import io.swagger.annotations.Api
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@Api(tags = ["03. Job Opening Info"], description = "구인구직 모집 서비스")
@RestController
@RequestMapping("/api/v1/job-openings")
class ScrapJobOpeningController(
    private val scrapJobOpeningFacade: ScrapJobOpeningFacade,
) {

    @PatchMapping("/{jobOpeningId}/scrap")
    @PreAuthorize("hasRole('USER')")
    suspend fun scrapJobOpening(
        principal: Principal,
        @PathVariable jobOpeningId: Long,
    ): CommonResponse<Unit> {
        val response = scrapJobOpeningFacade.scrapJobOpening(principal.name, jobOpeningId)

        return CommonResponse.success(response)
    }
}