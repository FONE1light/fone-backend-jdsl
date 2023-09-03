package com.fone.sms.presentation.controllers

import com.fone.common.response.CommonResponse
import com.fone.sms.application.SmsFacade
import com.fone.sms.presentation.dto.SMSSendRequest
import com.fone.sms.presentation.dto.SMSSendResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(tags = ["08. SMS"], description = "SMS 전송 서비스")
@RestController
@RequestMapping("/api/v1/sms")
class SmsController(private val smsService: SmsFacade) {
    @PostMapping("/send-sms")
    @ApiOperation(value = "SMS 전송 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = SMSSendResponse::class))]
    )
    suspend fun sendSms(@RequestBody request: SMSSendRequest): CommonResponse<SMSSendResponse> {
        return CommonResponse.success(smsService.sendSmsMessage(request))
    }
}
