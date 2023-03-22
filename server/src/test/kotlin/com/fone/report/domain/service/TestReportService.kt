package com.fone.report.domain.service

import com.fone.report.domain.entity.Report
import com.fone.report.domain.repository.TestReportRepositoryImpl
import com.fone.report.domain.repository.save
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random

@Service
class TestReportService(
    private val reportRepository: TestReportRepositoryImpl,
) {

    suspend fun getReportCount(): Long = reportRepository.getCount()

    @Transactional
    suspend fun createReportsAndFailWithAnnotation() {
        reportRepository.save(Report(Random.nextLong()))
        reportRepository.save(Report(Random.nextLong()))
        reportRepository.save(Report(Random.nextLong()))
        throw RuntimeException("FAIL!")
    }

    suspend fun createReportsAndFailWithTransactionalScope() = reportRepository.startTransaction { session, _ ->
        session.save(Report(Random.nextLong()))
        session.save(Report(Random.nextLong()))
        session.save(Report(Random.nextLong()))
        throw RuntimeException("FAIL!")
    }
}
