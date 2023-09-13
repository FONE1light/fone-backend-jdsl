package com.fone.report.application

import com.fone.report.domain.service.DiscordReportService
import com.fone.report.domain.service.RegisterReportService
import com.fone.report.presentation.dto.RegisterReportDto.RegisterReportRequest
import com.fone.report.presentation.dto.RegisterReportDto.RegisterReportResponse
import org.springframework.stereotype.Service

@Service
class RegisterReportFacade(
    private val registerReportService: RegisterReportService,
    private val discordReportService: DiscordReportService,
) {

    suspend fun registerReport(request: RegisterReportRequest, email: String): RegisterReportResponse {
        val report = registerReportService.registerReport(request, email)
        discordReportService.sendReport(report)
        return RegisterReportResponse(report)
    }
}
