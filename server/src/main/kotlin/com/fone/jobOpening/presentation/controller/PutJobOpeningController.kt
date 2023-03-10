package com.fone.jobOpening.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.jobOpening.presentation.dto.RegisterJobOpeningDto.RegisterJobOpeningRequest
import com.fone.jobOpening.presentation.dto.RegisterJobOpeningDto.RegisterJobOpeningResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.validation.Valid

@Api(tags = ["03. Job Opening Info"], description = "구인구직 모집 서비스")
@RestController
@RequestMapping("/api/v1/job-openings")
class PutJobOpeningController(
    private val putJobOpeningFacade: com.fone.jobOpening.application.PutJobOpeningFacade,
) {

    @PutMapping("/{jobOpeningId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "구인구직 수정 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공"
    )
    suspend fun putJobOpening(
        principal: Principal,
        @Valid @RequestBody
        request: RegisterJobOpeningRequest,
        @PathVariable jobOpeningId: Long,
    ): CommonResponse<RegisterJobOpeningResponse> {
        val response = putJobOpeningFacade.putJobOpening(request, principal.name, jobOpeningId)

        return CommonResponse.success(response)
    }
}
