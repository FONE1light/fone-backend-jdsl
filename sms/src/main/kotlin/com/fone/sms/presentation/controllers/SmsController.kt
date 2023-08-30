package com.fone.sms.presentation.controllers

import com.fone.sms.presentation.data.SmsRequest
import com.fone.sms.presentation.data.SmsResponse
import com.fone.sms.services.SmsService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sms")
class SmsController(private val smsService: SmsService) {
    @PostMapping("/send-sms")
    suspend fun sendSms(@RequestBody request: SmsRequest): SmsResponse {
        return smsService.sendSmsMessage(request)
    }
}
