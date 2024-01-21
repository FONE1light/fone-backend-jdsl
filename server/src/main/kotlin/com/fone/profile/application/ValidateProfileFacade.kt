package com.fone.profile.application

import com.fone.profile.domain.service.ValidateProfileService
import com.fone.profile.presentation.dto.ValidateProfileDto
import org.springframework.stereotype.Service

@Service
class ValidateProfileFacade(
    private val validateProfileService: ValidateProfileService,
) {

    suspend fun validateBasicPage(basicPageValidation: ValidateProfileDto.BasicPageValidation) =
        validateProfileService.validateBasicPage(basicPageValidation)

    suspend fun validateDetailPage(email: String, detailPageValidation: ValidateProfileDto.DetailPageValidation) =
        validateProfileService.validateDetailPage(email, detailPageValidation)

    suspend fun validateDescriptionPage(descriptionPageValidation: ValidateProfileDto.DescriptionPageValidation) =
        validateProfileService.validateDescriptionPage(descriptionPageValidation)

    suspend fun validateCareerPage(careerPageValidation: ValidateProfileDto.CareerPageValidation) =
        validateProfileService.validateCareerPage(careerPageValidation)

    suspend fun validateInterestPage(interestPageValidation: ValidateProfileDto.InterestPageValidation) =
        validateProfileService.validateInterestPage(interestPageValidation)
}
