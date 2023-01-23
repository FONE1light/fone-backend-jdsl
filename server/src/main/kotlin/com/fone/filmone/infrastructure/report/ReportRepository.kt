package com.fone.filmone.infrastructure.report

import com.fone.filmone.domain.report.entity.Report
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ReportRepository : CoroutineCrudRepository<Report, Long>