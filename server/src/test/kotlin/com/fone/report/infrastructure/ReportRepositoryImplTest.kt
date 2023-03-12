package com.fone.report.infrastructure

import com.fone.common.IntegrationTest
import com.fone.report.domain.entity.Report
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

@IntegrationTest
class ReportRepositoryImplTest(
    private val reportRepositoryImpl: ReportRepositoryImpl,
) : DescribeSpec({

    describe("save") {
        it("수정 테스트 성공") {
            val report = Report()
            reportRepositoryImpl.save(report)
            report.details = "detail test"
            val result = reportRepositoryImpl.save(report)

            result.details shouldBe "detail test"
        }

        it("저장 테스트 성공") {
            val report = Report()
            val result = reportRepositoryImpl.save(report)

            result shouldBe report
        }
    }
})
