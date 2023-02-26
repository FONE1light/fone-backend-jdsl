package com.fone.report.presentation.dto

import com.fone.report.domain.entity.Report
import com.fone.report.domain.enum.Type
import com.fone.report.presentation.dto.common.ReportDto

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
                inconvenients = inconvenients,
                details = details,
                userId = userId,
            )
        }
    }

    data class RegisterReportResponse(
        val report: ReportDto,
    ) {
        constructor(reqReport: Report) : this(report = ReportDto(reqReport))
    }
}
