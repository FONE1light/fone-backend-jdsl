package com.fone.report.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.report.domain.repository.ReportRepository
import com.fone.report.presentation.dto.RegisterReportDto.RegisterReportRequest
import com.fone.report.presentation.dto.RegisterReportDto.RegisterReportResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterReportService(
    private val reportRepository: ReportRepository,
    private val userRepository: UserCommonRepository
) {

    @Transactional
    suspend fun registerReport(
        request: RegisterReportRequest,
        email: String
    ): RegisterReportResponse {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        with(request) {
            val report = toEntity(userId)
            reportRepository.save(report)

            return RegisterReportResponse(report)
        }
    }
}
