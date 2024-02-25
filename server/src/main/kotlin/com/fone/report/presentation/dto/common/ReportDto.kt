package com.fone.report.presentation.dto.common

import com.fone.report.domain.entity.Report
import com.fone.report.domain.enum.Type
import io.swagger.v3.oas.annotations.media.Schema

data class ReportDto(
    @Schema(description = "id")
    val id: Long,
    @Schema(description = "신고한 유저 id")
    val reportUserId: Long,
    @Schema(description = "신고 유형 (채팅, 프로필, 모집글)")
    val type: Type,
    @Schema(description = "신고 유형 id")
    val typeId: Long,
    @Schema(description = "불편한 점 (최대 5개)")
    val inconvenients: List<String>,
    @Schema(description = "신고 상세 내용")
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
