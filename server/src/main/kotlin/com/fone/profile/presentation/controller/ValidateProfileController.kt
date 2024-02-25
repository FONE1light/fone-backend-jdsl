package com.fone.profile.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.jobOpening.application.ValidateJobOpeningFacade
import com.fone.jobOpening.presentation.dto.FirstPage
import com.fone.profile.application.ValidateProfileFacade
import com.fone.profile.presentation.dto.FifthPage
import com.fone.profile.presentation.dto.FourthPage
import com.fone.profile.presentation.dto.SecondPage
import com.fone.profile.presentation.dto.SixthPage
import com.fone.profile.presentation.dto.ThirdPage
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@Api(tags = ["04. Profile Info"], description = "프로필 서비스")
@RestController
@RequestMapping("/api/v1/profiles/validate")
class ValidateProfileController(
    private val validateProfileFacade: ValidateProfileFacade,
    private val validateJobOpeningFacade: ValidateJobOpeningFacade,
) {
    @PostMapping("/contact")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "프로필 검증 API - 페이지1")
    @ApiResponse(
        responseCode = "200",
        description = "성공"
    )
    suspend fun contactPageValidation(
        @RequestBody
        request: FirstPage,
    ): CommonResponse<Unit> {
        validateJobOpeningFacade.validateContactPage(request)
        return CommonResponse.success()
    }

    @PostMapping("/basic")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "프로필 검증 API - 페이지2")
    @ApiResponse(
        responseCode = "200",
        description = "성공"
    )
    suspend fun basicPageValidate(
        @RequestBody
        secondPage: SecondPage,
    ): CommonResponse<Unit> {
        validateProfileFacade.validateBasicPage(secondPage)
        return CommonResponse.success()
    }

    @PostMapping("/details")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "프로필 검증 API - 페이지3")
    @ApiResponse(
        responseCode = "200",
        description = "성공"
    )
    suspend fun detailPageValidate(
        principal: Principal,
        @RequestBody
        thirdPage: ThirdPage,
    ): CommonResponse<Unit> {
        validateProfileFacade.validateDetailPage(principal.name, thirdPage)
        return CommonResponse.success()
    }

    @PostMapping("/description")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "프로필 검증 API - 페이지4")
    @ApiResponse(
        responseCode = "200",
        description = "성공"
    )
    suspend fun descriptionValidate(
        @RequestBody
        fourthPage: FourthPage,
    ): CommonResponse<Unit> {
        validateProfileFacade.validateDescriptionPage(fourthPage)
        return CommonResponse.success()
    }

    @PostMapping("/career")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "프로필 검증 API - 페이지5")
    @ApiResponse(
        responseCode = "200",
        description = "성공"
    )
    suspend fun careerValidate(
        @RequestBody
        fifthPage: FifthPage,
    ): CommonResponse<Unit> {
        validateProfileFacade.validateCareerPage(fifthPage)
        return CommonResponse.success()
    }

    @PostMapping("/interest")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "프로필 검증 API - 페이지6")
    @ApiResponse(
        responseCode = "200",
        description = "성공"
    )
    suspend fun interestValidate(
        @RequestBody
        sixthPage: SixthPage,
    ): CommonResponse<Unit> {
        validateProfileFacade.validateInterestPage(sixthPage)
        return CommonResponse.success()
    }
}
