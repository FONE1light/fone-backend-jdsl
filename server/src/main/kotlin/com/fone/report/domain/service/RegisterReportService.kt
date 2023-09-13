package com.fone.report.domain.service

import com.fone.common.exception.NotFoundException
import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.jobOpening.domain.repository.JobOpeningRepository
import com.fone.profile.domain.repository.ProfileRepository
import com.fone.report.domain.entity.Report
import com.fone.report.domain.enum.Type
import com.fone.report.domain.repository.ReportRepository
import com.fone.report.presentation.dto.RegisterReportDto.RegisterReportRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterReportService(
    private val reportRepository: ReportRepository,
    private val profileRepository: ProfileRepository,
    private val jobOpeningRepository: JobOpeningRepository,
    private val userRepository: UserCommonRepository,
) {

    @Transactional
    suspend fun registerReport(
        request: RegisterReportRequest,
        email: String,
    ): Report {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()
        with(request) {
            val report = toEntity(userId)
            when (type) {
                Type.CHATTING -> {
                    TODO("Chatting 구현 필요")
                }
                Type.PROFILE -> {
                    val profile = profileRepository.findById(typeId) ?: throw NotFoundException("신고 대상을 찾지 못했습니다")
                    report.reportUserId = profile.userId
                }
                Type.JOB_OPENING -> {
                    val jobOpening = jobOpeningRepository.findById(typeId) ?: throw NotFoundException("신고 대상을 찾지 못했습니다")
                    report.reportUserId = jobOpening.userId
                }
            }
            return reportRepository.save(report)
        }
    }
}
