package com.fone.user.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.user.application.EmailValidationFacade
import com.fone.user.presentation.dto.EmailValidationDto
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
class EmailValidationController(private val emailValidationFacade: EmailValidationFacade) {
    @PostMapping("/email/send")
    @ApiOperation(value = "인증 이메일 전송 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = EmailValidationDto.EmailSendResponse::class))]
    )
    suspend fun sendEmail(
        @Valid @RequestBody
        request: EmailValidationDto.EmailSendRequest,
    ): CommonResponse<EmailValidationDto.EmailSendResponse> {
        val response = emailValidationFacade.sendValidationMessage(request)
        return CommonResponse.success(response)
    }

    @PostMapping("/email/validate")
    @ApiOperation(value = "이메일 인증 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = EmailValidationDto.EmailValidationResponse::class))]
    )
    suspend fun validateEmail(
        @Valid @RequestBody
        request: EmailValidationDto.EmailValidationRequest,
    ): CommonResponse<EmailValidationDto.EmailValidationResponse> {
        val response = emailValidationFacade.validateCode(request)
        return CommonResponse.success(response)
    }

    @PostMapping("/email/duplicate")
    @ApiOperation(value = "이메일 중복 확인 Api")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = EmailValidationDto.EmailDuplicationResponse::class))]
    )
    suspend fun isDuplicateEmail(
        @Valid @RequestBody
        request: EmailValidationDto.EmailDuplicationRequest,
    ): CommonResponse<EmailValidationDto.EmailDuplicationResponse> {
        val response = emailValidationFacade.duplicateCheck(request)
        return CommonResponse.success(response)
    }
}
