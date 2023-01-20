package com.fone.filmone.presentation.job_opening

import com.fone.filmone.application.job_opening.RetrieveJobOpeningFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.domain.common.Type
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
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
    ): CommonResponse<RetrieveJobOpeningDto.RetrieveJobOpeningsResponse> {
        val response = retrieveJobOpeningFacade.retrieveJobOpenings(principal.name, pageable, type)

        return CommonResponse.success(response)
    }
}