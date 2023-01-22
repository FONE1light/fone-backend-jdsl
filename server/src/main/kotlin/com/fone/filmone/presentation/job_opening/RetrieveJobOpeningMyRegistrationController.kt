package com.fone.filmone.presentation.job_opening

import com.fone.filmone.application.job_opening.RetrieveJobOpeningMyRegistrationFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.presentation.job_opening.RetrieveJobOpeningMyRegistrationDto.RetrieveJobOpeningMyRegistrationResponse
import io.swagger.annotations.Api
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal


@Api(tags = ["03. Job Opening Info"], description = "구인구직 모집 서비스")
@RestController
@RequestMapping("/api/v1/job-openings")
class RetrieveJobOpeningMyRegistrationController(
    private val retrieveJobOpeningMyRegistrationFacade: RetrieveJobOpeningMyRegistrationFacade,
) {

    @GetMapping("/my-registrations")
    @PreAuthorize("hasRole('USER')")
    suspend fun retrieveJobOpeningMyRegistrations(
        principal: Principal,
    ): CommonResponse<RetrieveJobOpeningMyRegistrationResponse> {
        val response =
            retrieveJobOpeningMyRegistrationFacade.retrieveJobOpeningMyRegistrations(principal.name)

        return CommonResponse.success(response)
    }
}