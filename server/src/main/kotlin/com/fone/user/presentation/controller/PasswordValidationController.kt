package com.fone.user.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.user.application.PasswordValidationFacade
import com.fone.user.presentation.dto.PasswordValidationRequest
import com.fone.user.presentation.dto.PasswordValidationResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Api(tags = ["01. User Info"], description = "유저 서비스")
@RestController
@RequestMapping("/api/v1/users")
class PasswordValidationController(
    private val passwordValidationFacade: PasswordValidationFacade,
) {

    @PostMapping("/password/validate")
    @ApiOperation(value = "로그인 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = PasswordValidationResponse::class))]
    )
    suspend fun validateSignInRequest(
        @Valid @RequestBody
        request: PasswordValidationRequest,
    ): CommonResponse<PasswordValidationResponse> {
        val response = passwordValidationFacade.validate(request)
        return CommonResponse.success(response)
    }
}
