package com.fone.report.application

import com.fone.report.domain.service.RegisterReportService
import com.fone.report.presentation.RegisterReportDto.RegisterReportRequest
import org.springframework.stereotype.Service

@Service
class RegisterReportFacade(
    private val registerReportService: RegisterReportService,
) {

    suspend fun registerReport(request: RegisterReportRequest, email: String) =
        registerReportService.registerReport(request, email)
}