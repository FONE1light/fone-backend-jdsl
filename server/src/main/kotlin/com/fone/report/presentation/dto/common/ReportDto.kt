package com.fone.report.presentation.dto.common

import com.fone.report.domain.entity.Report
import com.fone.report.domain.enum.Type

data class ReportDto(
    val id: Long,
    val reportUserId: Long,
    val type: Type,
    val typeId: Long,
    val inconvenients: List<String>,
    val details: String,
) {
    constructor(
        report: Report,
    ) : this(
        id = report.id!!,
        reportUserId = report.reportUserId,
        type = report.type,
        typeId = report.typeId,
        inconvenients = report.inconvenients,
        details = report.details
    )
}
