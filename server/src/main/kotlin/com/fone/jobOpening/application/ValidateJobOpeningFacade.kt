package com.fone.jobOpening.application

import com.fone.jobOpening.domain.service.ValidateJobOpeningService
import com.fone.jobOpening.presentation.dto.ValidateJobOpeningDto
import org.springframework.stereotype.Service

@Service
class ValidateJobOpeningFacade(
    private val validateJobOpeningService: ValidateJobOpeningService,
) {
    suspend fun validateContactPage(request: ValidateJobOpeningDto.FirstPage) =
        validateJobOpeningService.validateContactPage(request)

    suspend fun validateTitlePage(request: ValidateJobOpeningDto.SecondPage) =
        validateJobOpeningService.validateTitlePage(request)

    suspend fun validateRolePage(request: ValidateJobOpeningDto.ThirdPage) =
        validateJobOpeningService.validateRolePage(request)

    suspend fun validateProjectPage(request: ValidateJobOpeningDto.FourthPage) =
        validateJobOpeningService.validateProjectPage(request)

    suspend fun validateProjectDetailsPage(request: ValidateJobOpeningDto.FifthPage) =
        validateJobOpeningService.validateProjectDetailsPage(request)

    suspend fun validateSummaryPage(request: ValidateJobOpeningDto.SixthPage) =
        validateJobOpeningService.validateSummaryPage(request)

    suspend fun validateManagerPage(request: ValidateJobOpeningDto.SeventhPage) =
        validateJobOpeningService.validateManagerPage(request)
}
