package com.fone.jobOpening.application

import com.fone.jobOpening.domain.service.ValidateJobOpeningService
import com.fone.jobOpening.presentation.dto.FifthPage
import com.fone.jobOpening.presentation.dto.FirstPage
import com.fone.jobOpening.presentation.dto.FourthPage
import com.fone.jobOpening.presentation.dto.SecondPage
import com.fone.jobOpening.presentation.dto.SeventhPage
import com.fone.jobOpening.presentation.dto.SixthPage
import com.fone.jobOpening.presentation.dto.ThirdPage
import org.springframework.stereotype.Service

@Service
class ValidateJobOpeningFacade(
    private val validateJobOpeningService: ValidateJobOpeningService,
) {
    suspend fun validateContactPage(request: FirstPage) =
        validateJobOpeningService.validateContactPage(request)

    suspend fun validateTitlePage(request: SecondPage) =
        validateJobOpeningService.validateTitlePage(request)

    suspend fun validateRolePage(request: ThirdPage) =
        validateJobOpeningService.validateRolePage(request)

    suspend fun validateProjectPage(request: FourthPage) =
        validateJobOpeningService.validateProjectPage(request)

    suspend fun validateProjectDetailsPage(request: FifthPage) =
        validateJobOpeningService.validateProjectDetailsPage(request)

    suspend fun validateSummaryPage(request: SixthPage) =
        validateJobOpeningService.validateSummaryPage(request)

    suspend fun validateManagerPage(request: SeventhPage) =
        validateJobOpeningService.validateManagerPage(request)
}
