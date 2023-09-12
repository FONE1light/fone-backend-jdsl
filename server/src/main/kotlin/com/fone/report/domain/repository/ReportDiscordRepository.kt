package com.fone.report.domain.repository

import com.fone.report.domain.entity.Report

interface ReportDiscordRepository {
    suspend fun send(report: Report)
}
