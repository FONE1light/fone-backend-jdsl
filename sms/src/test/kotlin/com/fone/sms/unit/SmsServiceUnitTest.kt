package com.fone.sms.unit

import com.fasterxml.jackson.databind.ObjectMapper
import com.fone.sms.application.SmsFacade
import com.fone.sms.domain.dto.AligoSmsRequest
import com.fone.sms.domain.dto.AligoSmsResponse
import com.fone.sms.domain.service.AligoService
import com.fone.sms.presentation.dto.SMSSendRequest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.spyk

class SmsServiceUnitTest : FreeSpec() {
    private val aligoServiceMock: AligoService = spyk(AligoService("", "", "", ObjectMapper()))
    private val service = SmsFacade(aligoServiceMock)

    init {
        "SmsService Unit Test" {
            val phone = "010-1234-5678"
            val code = "2424"
            val mockResponse = AligoSmsResponse(0, "", "123", 1, 0)
            coEvery {
                aligoServiceMock.sendToAligo(any<AligoSmsRequest>())
            } answers {
                val message = it.invocation.args.first() as AligoSmsRequest
                message.receiver shouldBe phone
                message.msg shouldContain code
                mockResponse
            }
            val response = service.sendSmsMessage(SMSSendRequest(phone, code))

            coVerify {
                aligoServiceMock.sendToAligo(any<AligoSmsRequest>())
            }
        }
    }
}
