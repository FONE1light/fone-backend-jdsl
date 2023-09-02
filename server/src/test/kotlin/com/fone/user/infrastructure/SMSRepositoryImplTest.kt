package com.fone.user.infrastructure

import com.fone.sms.application.SmsFacade
import io.kotest.core.spec.style.FreeSpec
import io.mockk.coEvery
import io.mockk.mockk

class SMSRepositoryImplTest : FreeSpec({
    "SMS의 경우" - {
        "문자를 전송 할 수 있어아함".config(enabled = false) {
            val mockSmsFacade: SmsFacade = mockk()
            val repo = SMSRepositoryImpl(mockSmsFacade)
            coEvery { mockSmsFacade.sendSmsMessage(any()) } returns mockk()
            repo.sendValidationMessage("010-3210-3287", "123456")
        }
    }
})
