package com.fone.report.presentation.dto

import com.fone.report.domain.entity.Report
import com.fone.report.domain.enum.Type
import com.fone.report.presentation.dto.common.ReportDto

data class RegisterReportRequest(
    val type: Type,
    val typeId: Long,
    val inconveniences: List<String>,
    val details: String,
) {
    fun toEntity(userId: Long): Report {
        return Report(
            type = type,
            typeId = typeId,
            inconvenients = inconveniences,
            details = details,
            userId = userId
        )
    }
}

data class RegisterReportResponse(
    val report: ReportDto,
) {
    constructor(reqReport: Report) : this(report = ReportDto(reqReport))
}
