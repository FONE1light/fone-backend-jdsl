package com.fone.jobOpening.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.jobOpening.application.RetrieveJobOpeningMyRegistrationFacade
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningMyRegistrationDto.RetrieveJobOpeningMyRegistrationResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@Api(tags = ["03. Job Opening Info"], description = "구인구직 모집 서비스")
@RestController
@RequestMapping("/api/v1/job-openings")
class RetrieveJobOpeningMyRegistrationController(
    private val retrieveJobOpeningMyRegistrationFacade: RetrieveJobOpeningMyRegistrationFacade
) {

    @GetMapping("/my-registrations")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "내가 등록한 구인구직 조회 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공"
    )
    suspend fun retrieveJobOpeningMyRegistrations(
        pageable: Pageable,
        principal: Principal
    ): CommonResponse<RetrieveJobOpeningMyRegistrationResponse> {
        val response =
            retrieveJobOpeningMyRegistrationFacade.retrieveJobOpeningMyRegistrations(
                pageable,
                principal.name
            )

        return CommonResponse.success(response)
    }
}
