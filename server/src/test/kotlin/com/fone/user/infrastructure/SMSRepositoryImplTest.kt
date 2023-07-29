package com.fone.user.infrastructure

import io.kotest.core.spec.style.FreeSpec

class SMSRepositoryImplTest : FreeSpec({
    "SMS의 경우" - {
        "문자를 전송 할 수 있어아함".config(enabled = false) {
            val repo = SMSRepositoryImpl("https://du646e9qh1.execute-api.ap-northeast-2.amazonaws.com/prod/send-sms")
            repo.sendValidationMessage("010-3210-3287", "123456")
        }
    }
})
