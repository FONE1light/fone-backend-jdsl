package com.fone.filmone.domain.report.service

import com.fone.common.exception.NotFoundUserException
import com.fone.filmone.domain.report.repository.ReportRepository
import com.fone.filmone.domain.user.repository.UserRepository
import com.fone.filmone.presentation.report.RegisterReportDto.RegisterReportRequest
import com.fone.filmone.presentation.report.RegisterReportDto.RegisterReportResponse
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
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()

        with(request) {
            val report = toEntity(user.id!!)
            reportRepository.save(report)

            return RegisterReportResponse(report)
        }
    }
}