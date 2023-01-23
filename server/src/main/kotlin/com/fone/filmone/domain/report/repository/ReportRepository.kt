package com.fone.filmone.domain.report.repository

import com.fone.filmone.domain.report.entity.Report

interface ReportRepository {
    suspend fun save(report: Report): Report
}