package com.fone.jobOpening.application

import com.fone.jobOpening.domain.service.ValidateJobOpeningService
import com.fone.jobOpening.presentation.dto.ValidateJobOpeningDto
import org.springframework.stereotype.Service

@Service
class ValidateJobOpeningFacade(
    private val validateJobOpeningService: ValidateJobOpeningService,
) {
    suspend fun validateContactPage(request: ValidateJobOpeningDto.ContactPageValidation) =
        validateJobOpeningService.validateContactPage(request)

    suspend fun validateTitlePage(request: ValidateJobOpeningDto.TitlePageValidation) =
        validateJobOpeningService.validateTitlePage(request)

    suspend fun validateRolePage(request: ValidateJobOpeningDto.RolePageValidation) =
        validateJobOpeningService.validateRolePage(request)

    suspend fun validateProjectPage(request: ValidateJobOpeningDto.ProjectPageValidation) =
        validateJobOpeningService.validateProjectPage(request)

    suspend fun validateProjectDetailsPage(request: ValidateJobOpeningDto.ProjectDetailsPageValidation) =
        validateJobOpeningService.validateProjectDetailsPage(request)

    suspend fun validateSummaryPage(request: ValidateJobOpeningDto.SummaryPageValidation) =
        validateJobOpeningService.validateSummaryPage(request)

    suspend fun validateManagerPage(request: ValidateJobOpeningDto.ManagerInfoValidation) =
        validateJobOpeningService.validateManagerPage(request)
}
