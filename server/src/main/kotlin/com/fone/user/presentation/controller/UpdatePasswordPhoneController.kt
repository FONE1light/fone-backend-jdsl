package com.fone.user.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.user.application.PasswordPhoneValidationFacade
import com.fone.user.presentation.dto.PasswordPhoneDto.PasswordPhoneSMSRequest
import com.fone.user.presentation.dto.PasswordPhoneDto.PasswordPhoneSMSResponse
import com.fone.user.presentation.dto.PasswordPhoneDto.PasswordPhoneValidationRequest
import com.fone.user.presentation.dto.PasswordPhoneDto.PasswordPhoneValidationResponse
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
class UpdatePasswordPhoneController(private val passwordPhoneValidationFacade: PasswordPhoneValidationFacade) {
    @PostMapping("/password/sms")
    @ApiOperation(value = "비밀번호 변경 SMS 전송 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = PasswordPhoneSMSResponse::class))]
    )
    suspend fun passwordSendSMS(
        @Valid @RequestBody
        request: PasswordPhoneSMSRequest,
    ): CommonResponse<PasswordPhoneSMSResponse> {
        val response = passwordPhoneValidationFacade.sendSMS(request)
        return CommonResponse.success(response)
    }

    @PostMapping("/password/sms-validate")
    @ApiOperation(value = "비밀번호 변경 SMS 인증 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = PasswordPhoneValidationResponse::class))]
    )
    suspend fun passwordValidateSMS(
        @Valid @RequestBody
        request: PasswordPhoneValidationRequest,
    ): CommonResponse<PasswordPhoneValidationResponse> {
        val response = passwordPhoneValidationFacade.validate(request)
        return CommonResponse.success(response)
    }
}
