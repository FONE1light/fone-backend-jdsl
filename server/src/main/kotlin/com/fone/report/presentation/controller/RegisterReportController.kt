package com.fone.report.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.question.presentation.dto.RegisterQuestionDto
import com.fone.report.application.RegisterReportFacade
import com.fone.report.presentation.dto.RegisterReportDto.RegisterReportRequest
import com.fone.report.presentation.dto.RegisterReportDto.RegisterReportResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.validation.Valid

@Api(tags = ["06. Report Info"], description = "신고 서비스")
@RestController
@RequestMapping("/api/v1/reports")
class RegisterReportController(
    private val registerReportFacade: RegisterReportFacade,
) {

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "신고 등록 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = RegisterReportResponse::class))]
    )
    suspend fun registerReport(
        principal: Principal,
        @Valid @RequestBody
        request: RegisterReportRequest,
    ): CommonResponse<RegisterReportResponse> {
        val response = registerReportFacade.registerReport(request, principal.name)
        return CommonResponse.success(response)
    }
}
