package com.fone.profile.application

import com.fone.profile.domain.service.ValidateProfileService
import com.fone.profile.presentation.dto.ValidateProfileDto
import org.springframework.stereotype.Service

@Service
class ValidateProfileFacade(
    private val validateProfileService: ValidateProfileService,
) {

    suspend fun validateBasicPage(secondPage: ValidateProfileDto.SecondPage) =
        validateProfileService.validateBasicPage(secondPage)

    suspend fun validateDetailPage(email: String, thirdPage: ValidateProfileDto.ThirdPage) =
        validateProfileService.validateDetailPage(email, thirdPage)

    suspend fun validateDescriptionPage(fourthPage: ValidateProfileDto.FourthPage) =
        validateProfileService.validateDescriptionPage(fourthPage)

    suspend fun validateCareerPage(fifthPage: ValidateProfileDto.FifthPage) =
        validateProfileService.validateCareerPage(fifthPage)

    suspend fun validateInterestPage(sixthPage: ValidateProfileDto.SixthPage) =
        validateProfileService.validateInterestPage(sixthPage)
}
