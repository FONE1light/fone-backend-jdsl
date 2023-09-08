package com.fone.sms.domain.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AligoSmsResponse(
    val resultCode: Int,
    val message: String,
    val msgId: String?,
    val successCnt: Int?,
    val errorCnt: Int?,
    val msgType: String = "SMS",
)
