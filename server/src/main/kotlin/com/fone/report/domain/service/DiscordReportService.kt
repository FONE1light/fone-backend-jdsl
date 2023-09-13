package com.fone.report.domain.service

import com.fone.report.domain.entity.Report
import com.fone.report.domain.repository.ReportDiscordRepository
import org.springframework.stereotype.Service

@Service
class DiscordReportService(
    private val reportDiscordRepository: ReportDiscordRepository,
) {
    suspend fun sendReport(report: Report) = reportDiscordRepository.send(report)
}
