package com.fone.filmone.presentation.report

import com.fone.filmone.domain.report.entity.Report
import com.fone.filmone.domain.report.enum.Type

class RegisterReportDto {

    data class RegisterReportRequest(
        val reportUserId: Long,
        val type: Type,
        val typeId: Long,
        val inconvenients: List<String>,
        val details: String,
    ) {
        fun toEntity(userId: Long): Report {

            return Report(
                reportUserId = reportUserId,
                type = type.toString(),
                typeId = typeId,
                inconvenients = inconvenients.joinToString(","),
                details = details,
                userId = userId,
            )
        }
    }

    data class RegisterReportResponse(
        val report: ReportDto,
    ) {
        constructor(reqReport: Report) : this(
            report = ReportDto(reqReport)
        )
    }
}