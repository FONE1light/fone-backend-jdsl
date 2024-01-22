package com.fone.profile.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.profile.application.ValidateProfileFacade
import com.fone.profile.presentation.dto.ValidateProfileDto
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
) {
    @PostMapping("/basic")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "프로필 검증 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공"
    )
    suspend fun basicPageValidate(
        @RequestBody
        basicPageValidation: ValidateProfileDto.BasicPageValidation,
    ): CommonResponse<Unit> {
        validateProfileFacade.validateBasicPage(basicPageValidation)
        return CommonResponse.success()
    }

    @PostMapping("/details")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "프로필 검증 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공"
    )
    suspend fun detailPageValidate(
        principal: Principal,
        @RequestBody
        detailPageValidation: ValidateProfileDto.DetailPageValidation,
    ): CommonResponse<Unit> {
        validateProfileFacade.validateDetailPage(principal.name, detailPageValidation)
        return CommonResponse.success()
    }

    @PostMapping("/description")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "프로필 검증 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공"
    )
    suspend fun descriptionValidate(
        @RequestBody
        descriptionPageValidation: ValidateProfileDto.DescriptionPageValidation,
    ): CommonResponse<Unit> {
        validateProfileFacade.validateDescriptionPage(descriptionPageValidation)
        return CommonResponse.success()
    }

    @PostMapping("/career")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "프로필 검증 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공"
    )
    suspend fun careerValidate(
        @RequestBody
        careerPageValidation: ValidateProfileDto.CareerPageValidation,
    ): CommonResponse<Unit> {
        validateProfileFacade.validateCareerPage(careerPageValidation)
        return CommonResponse.success()
    }

    @PostMapping("/interest")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "프로필 검증 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공"
    )
    suspend fun interestValidate(
        @RequestBody
        interestPageValidation: ValidateProfileDto.InterestPageValidation,
    ): CommonResponse<Unit> {
        validateProfileFacade.validateInterestPage(interestPageValidation)
        return CommonResponse.success()
    }
}
