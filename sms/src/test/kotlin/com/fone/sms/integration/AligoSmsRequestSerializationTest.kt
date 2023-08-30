package com.fone.sms.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fone.sms.data.AligoSmsRequest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.string.shouldContain
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AligoSmsRequestSerializationTest(private val objectMapper: ObjectMapper) : FreeSpec() {
    override fun extensions() = listOf(SpringExtension)

    init {
        "AligoSmsRequest Serialization Test" - {
            "Should match snakecase" {
                val json = objectMapper.writeValueAsString(AligoSmsRequest("", "", "", "", ""))
                json shouldContain "user_id"
                json shouldContain "msg_type"
            }
        }
    }
}
