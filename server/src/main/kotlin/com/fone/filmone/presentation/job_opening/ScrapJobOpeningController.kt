package com.fone.filmone.presentation.job_opening

import com.fone.filmone.application.job_opening.ScrapJobOpeningFacade
import com.fone.filmone.common.response.CommonResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/v1/job-openings")
class ScrapJobOpeningController(
    private val scrapJobOpeningFacade: ScrapJobOpeningFacade,
) {

    @PatchMapping("/{jobOpeningId}")
    @PreAuthorize("hasRole('USER')")
    suspend fun scrapJobOpening(
        principal: Principal,
        @PathVariable jobOpeningId: Long,
    ): CommonResponse<Unit> {
        val response = scrapJobOpeningFacade.scrapJobOpening(principal.name, jobOpeningId)

        return CommonResponse.success(response)
    }
}