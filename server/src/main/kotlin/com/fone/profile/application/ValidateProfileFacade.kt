package com.fone.profile.application

import com.fone.profile.domain.service.ValidateProfileService
import com.fone.profile.presentation.dto.FifthPage
import com.fone.profile.presentation.dto.FourthPage
import com.fone.profile.presentation.dto.SecondPage
import com.fone.profile.presentation.dto.SixthPage
import com.fone.profile.presentation.dto.ThirdPage
import org.springframework.stereotype.Service

@Service
class ValidateProfileFacade(
    private val validateProfileService: ValidateProfileService,
) {

    suspend fun validateBasicPage(secondPage: SecondPage) =
        validateProfileService.validateBasicPage(secondPage)

    suspend fun validateDetailPage(email: String, thirdPage: ThirdPage) =
        validateProfileService.validateDetailPage(email, thirdPage)

    suspend fun validateDescriptionPage(fourthPage: FourthPage) =
        validateProfileService.validateDescriptionPage(fourthPage)

    suspend fun validateCareerPage(fifthPage: FifthPage) =
        validateProfileService.validateCareerPage(fifthPage)

    suspend fun validateInterestPage(sixthPage: SixthPage) =
        validateProfileService.validateInterestPage(sixthPage)
}
