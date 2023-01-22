package com.fone.filmone.presentation.job_opening

import com.fone.filmone.application.job_opening.RetrieveJobOpeningScrapFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.domain.common.Type
import com.fone.filmone.presentation.job_opening.RetrieveJobOpeningScrapDto.RetrieveJobOpeningScrapResponse
import io.swagger.annotations.Api
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@Api(tags = ["03. Job Opening Info"], description = "구인구직 모집 서비스")
@RestController
@RequestMapping("/api/v1/job-openings")
class RetrieveJobOpeningScrapController(
    private val retrieveJobOpeningScrapFacade: RetrieveJobOpeningScrapFacade,
) {


    @GetMapping("/scraps")
    @PreAuthorize("hasRole('USER')")
    suspend fun retrieveJobOpeningScrap(
        principal: Principal,
        @RequestParam type: Type,
    ): CommonResponse<RetrieveJobOpeningScrapResponse> {
        val response = retrieveJobOpeningScrapFacade.retrieveJobOpeningScrap(
            principal.name,
            type,
        )

        return CommonResponse.success(response)
    }
}