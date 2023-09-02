package com.fone.sms.unit

import com.fone.sms.application.SmsFacade
import com.fone.sms.domain.data.AligoSmsRequest
import com.fone.sms.domain.data.AligoSmsResponse
import com.fone.sms.domain.service.AligoService
import com.fone.sms.presentation.data.Result
import com.fone.sms.presentation.data.SMSVerificationRequest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class SmsServiceUnitTest : FreeSpec() {
    private val aligoServiceMock: AligoService = mockk()
    private val service = SmsFacade(aligoServiceMock)

    init {
        "SmsService Unit Test" {
            val phone = "010-1234-5678"
            val code = "2424"
            val mockResponse = AligoSmsResponse(0, "", "123", 1, 0)
            coEvery {
                aligoServiceMock.sendToAligo(any())
            } answers {
                val message = it.invocation.args.first() as AligoSmsRequest
                message.receiver shouldBe phone
                message.msg shouldContain code
                mockResponse
            }
            val response = service.sendSmsMessage(SMSVerificationRequest(phone, code))
            response.result shouldBe Result.SUCCESS
            response.message shouldBe "인증번호를 전송하였습니다."
            coVerify {
                aligoServiceMock.sendToAligo(any())
            }
        }
    }
}
