package com.fone.report.domain.repository

import com.fone.report.domain.entity.Report

interface ReportRepository {
    suspend fun save(report: Report): Report
}
