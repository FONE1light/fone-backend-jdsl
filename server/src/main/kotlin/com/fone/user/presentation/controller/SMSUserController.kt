package com.fone.user.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.user.application.SMSUserFacade
import com.fone.user.presentation.dto.PasswordSMSValidationResponse
import com.fone.user.presentation.dto.SMSRequest
import com.fone.user.presentation.dto.SMSValidationRequest
import com.fone.user.presentation.dto.UserInfoSMSValidationResponse
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
class SMSUserController(private val smsUserFacade: SMSUserFacade) {
    @PostMapping("/sms/send")
    @ApiOperation(value = "인증 SMS 전송 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공"
    )
    suspend fun sendSMS(
        @Valid @RequestBody
        request: SMSRequest,
    ): CommonResponse<Unit> {
        smsUserFacade.sendSMS(request)
        return CommonResponse.success(Unit, "인증번호 전송했습니다.")
    }

    @PostMapping("/sms/find-id")
    @ApiOperation(value = "인증 SMS 전송 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = UserInfoSMSValidationResponse::class))]
    )
    suspend fun userInfoValidateSMS(
        @Valid @RequestBody
        request: SMSValidationRequest,
    ): CommonResponse<UserInfoSMSValidationResponse> {
        val response = smsUserFacade.validateUserInfo(request)
        return CommonResponse.success(response)
    }

    @PostMapping("/sms/find-password")
    @ApiOperation(value = "비밀번호 변경 SMS 인증 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = PasswordSMSValidationResponse::class))]
    )
    suspend fun passwordValidateSMS(
        @Valid @RequestBody
        request: SMSValidationRequest,
    ): CommonResponse<PasswordSMSValidationResponse> {
        val response = smsUserFacade.validatePassword(request)
        return CommonResponse.success(response)
    }
}
