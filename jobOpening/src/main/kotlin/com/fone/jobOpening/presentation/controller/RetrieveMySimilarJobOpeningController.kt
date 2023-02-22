package com.fone.jobOpening.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.jobOpening.application.RetrieveMySimilarJobOpeningFacade
import com.fone.jobOpening.presentation.dto.RetrieveMySimilarJobOpeningDto.RetrieveMySimilarJobOpeningResponse
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
@RequestMapping("/api/v1/job-openings/my-similar")
class RetrieveMySimilarJobOpeningController(
    private val retrieveMySimilarJobOpeningFacade: RetrieveMySimilarJobOpeningFacade,
) {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "나와 비슷한 사람들이 보고 있는 공고 조회 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
    )
    suspend fun retrieveMySimilarJobOpening(pageable: Pageable, principal: Principal):
            CommonResponse<RetrieveMySimilarJobOpeningResponse> {
        val response = retrieveMySimilarJobOpeningFacade.retrieveMySimilarJobOpening(
            pageable,
            principal.name
        )

        return CommonResponse.success(response)
    }
}