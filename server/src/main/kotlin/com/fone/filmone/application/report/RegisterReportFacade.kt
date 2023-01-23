package com.fone.filmone.application.report

import com.fone.filmone.domain.report.service.RegisterReportService
import com.fone.filmone.presentation.report.RegisterReportDto.RegisterReportRequest
import org.springframework.stereotype.Service

@Service
class RegisterReportFacade(
    private val registerReportService: RegisterReportService,
) {

    suspend fun registerReport(request: RegisterReportRequest, email: String) =
        registerReportService.registerReport(request, email)
}