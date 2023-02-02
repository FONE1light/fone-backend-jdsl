package com.fone.filmone.presentation.job_opening

import com.fone.common.response.CommonResponse
import com.fone.filmone.application.job_opening.RetrieveJobOpeningFacade
import com.fone.filmone.domain.common.Type
import com.fone.filmone.presentation.job_opening.RetrieveJobOpeningDto.*
import io.swagger.annotations.Api
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal


@Api(tags = ["03. Job Opening Info"], description = "구인구직 모집 서비스")
@RestController
@RequestMapping("/api/v1/job-openings")
class RetrieveJobOpeningController(
    private val retrieveJobOpeningFacade: RetrieveJobOpeningFacade,
) {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    suspend fun retrieveJobOpenings(
        principal: Principal,
        @ModelAttribute request: RetrieveJobOpeningsRequest,
        pageable: Pageable,
    ): CommonResponse<RetrieveJobOpeningsResponse> {
        val response = retrieveJobOpeningFacade.retrieveJobOpenings(
            principal.name,
            pageable,
            request)

        return CommonResponse.success(response)
    }

    @GetMapping("/{jobOpeningId}")
    @PreAuthorize("hasRole('USER')")
    suspend fun retrieveJobOpening(
        principal: Principal,
        @RequestParam type: Type,
        @PathVariable jobOpeningId: Long,
    ): CommonResponse<RetrieveJobOpeningResponse> {
        val response = retrieveJobOpeningFacade.retrieveJobOpening(
            principal.name,
            type,
            jobOpeningId
        )

        return CommonResponse.success(response)
    }
}