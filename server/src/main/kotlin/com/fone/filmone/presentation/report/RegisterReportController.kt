package com.fone.filmone.presentation.report

import com.fone.filmone.application.report.RegisterReportFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.presentation.report.RegisterReportDto.RegisterReportRequest
import com.fone.filmone.presentation.report.RegisterReportDto.RegisterReportResponse
import io.swagger.annotations.Api
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
    suspend fun registerReport(
        principal: Principal,
        @Valid @RequestBody request: RegisterReportRequest,
    ): CommonResponse<RegisterReportResponse> {
        val response = registerReportFacade.registerReport(request, principal.name)
        return CommonResponse.success(response)
    }
}