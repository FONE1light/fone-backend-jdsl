package com.fone.filmone.domain.report.service

import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.infrastructure.report.ReportRepository
import com.fone.filmone.infrastructure.user.UserRepository
import com.fone.filmone.presentation.report.RegisterReportDto.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterReportService(
    private val reportRepository: ReportRepository,
    private val userRepository: UserRepository,
) {

    @Transactional
    suspend fun registerReport(
        request: RegisterReportRequest,
        email: String,
    ): RegisterReportResponse {
        val user = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        with(request) {
            val report = toEntity(user.id!!)
            reportRepository.save(report)

            return RegisterReportResponse(report)
        }
    }
}