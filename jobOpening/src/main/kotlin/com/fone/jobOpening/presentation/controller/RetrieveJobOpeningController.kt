package com.fone.jobOpening.presentation.controller

import com.fone.common.entity.Type
import com.fone.common.response.CommonResponse
import com.fone.jobOpening.application.RetrieveJobOpeningFacade
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningDto.*
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.responses.ApiResponse
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
    @ApiOperation(value = "구인구직 리스트 조회 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
    )
    suspend fun retrieveJobOpenings(
        principal: Principal,
        @ModelAttribute request: RetrieveJobOpeningsRequest,
        pageable: Pageable,
    ): CommonResponse<RetrieveJobOpeningsResponse> {
        val response = retrieveJobOpeningFacade.retrieveJobOpenings(
            principal.name,
            pageable,
            request
        )

        return CommonResponse.success(response)
    }

    @GetMapping("/{jobOpeningId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "구인구직 디테일 조회 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
    )
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