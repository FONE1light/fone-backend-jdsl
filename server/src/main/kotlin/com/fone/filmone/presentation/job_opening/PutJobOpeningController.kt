package com.fone.filmone.presentation.job_opening

import com.fone.filmone.application.job_opening.PutJobOpeningFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.presentation.job_opening.RegisterJobOpeningDto.RegisterJobOpeningRequest
import com.fone.filmone.presentation.job_opening.RegisterJobOpeningDto.RegisterJobOpeningResponse
import io.swagger.annotations.Api
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.validation.Valid

@Api(tags = ["03. Job Opening Info"], description = "구인구직 모집 서비스")
@RestController
@RequestMapping("/api/v1/job-openings")
class PutJobOpeningController(
    private val putJobOpeningFacade: PutJobOpeningFacade,
) {

    @PutMapping("/{jobOpeningId}")
    @PreAuthorize("hasRole('USER')")
    suspend fun putJobOpening(
        principal: Principal,
        @Valid @RequestBody request: RegisterJobOpeningRequest,
        @PathVariable jobOpeningId: Long,
    ): CommonResponse<RegisterJobOpeningResponse> {
        val response = putJobOpeningFacade.putJobOpening(request, principal.name, jobOpeningId)

        return CommonResponse.success(response)
    }
}