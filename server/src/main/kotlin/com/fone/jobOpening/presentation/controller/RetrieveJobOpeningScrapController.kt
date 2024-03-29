package com.fone.jobOpening.presentation.controller

import com.fone.common.entity.Type
import com.fone.common.response.CommonResponse
import com.fone.jobOpening.application.RetrieveJobOpeningScrapFacade
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningScrapResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.data.domain.Pageable
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
    @ApiOperation(value = "내가 스크랩한 구인구직 조회 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = RetrieveJobOpeningScrapResponse::class))]
    )
    suspend fun retrieveJobOpeningScrap(
        pageable: Pageable,
        principal: Principal,
        @RequestParam(required = false) type: Type?,
    ): CommonResponse<RetrieveJobOpeningScrapResponse> {
        val response = retrieveJobOpeningScrapFacade.retrieveJobOpeningScrap(
            pageable,
            principal.name,
            type
        )

        return CommonResponse.success(response)
    }
}
