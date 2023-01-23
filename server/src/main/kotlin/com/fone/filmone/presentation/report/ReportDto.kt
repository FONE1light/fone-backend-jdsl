package com.fone.filmone.presentation.report

import com.fone.filmone.domain.report.entity.Report
import com.fone.filmone.domain.report.enum.Type

data class ReportDto (
    val id: Long,
    val reportUserId: Long,
    val type: Type,
    val typeId: Long,
    val inconvenients: String,
    val details: String,
) {
    constructor(report: Report) : this(
        id = report.id!!,
        reportUserId = report.reportUserId,
        type = Type(report.type),
        typeId = report.typeId,
        inconvenients = report.inconvenients,
        details = report.details,
    )
}