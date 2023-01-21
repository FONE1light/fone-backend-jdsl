package com.fone.filmone.presentation.job_opening

import com.fone.filmone.application.job_opening.RegisterJobOpeningFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.presentation.job_opening.RegisterJobOpeningDto.RegisterJobOpeningRequest
import com.fone.filmone.presentation.job_opening.RegisterJobOpeningDto.RegisterJobOpeningResponse
import io.swagger.annotations.Api
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.validation.Valid

@Api(tags = ["03. Job Opening Info"], description = "구인구직 모집 서비스")
@RestController
@RequestMapping("/api/v1/job-openings")
class RegisterJobOpeningController(
    private val registerJobOpeningFacade: RegisterJobOpeningFacade,
) {

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    suspend fun registerJobOpening(
        principal: Principal,
        @Valid @RequestBody request: RegisterJobOpeningRequest,
    ):
            CommonResponse<RegisterJobOpeningResponse> {
        val response = registerJobOpeningFacade.registerJobOpening(request, principal.name)
        return CommonResponse.success(response)
    }
}