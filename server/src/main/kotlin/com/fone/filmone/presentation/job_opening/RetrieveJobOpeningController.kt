package com.fone.filmone.presentation.job_opening

import com.fone.filmone.application.job_opening.RetrieveJobOpeningFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.domain.common.Type
import com.fone.filmone.presentation.job_opening.RetrieveJobOpeningDto.RetrieveJobOpeningResponse
import com.fone.filmone.presentation.job_opening.RetrieveJobOpeningDto.RetrieveJobOpeningsResponse
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal


@RestController
@RequestMapping("/api/v1/job-openings")
class RetrieveJobOpeningController(
    private val retrieveJobOpeningFacade: RetrieveJobOpeningFacade,
) {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    suspend fun retrieveJobOpenings(
        principal: Principal,
        @RequestParam type: Type,
        pageable: Pageable,
    ): CommonResponse<RetrieveJobOpeningsResponse> {
        val response = retrieveJobOpeningFacade.retrieveJobOpenings(principal.name, pageable, type)

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