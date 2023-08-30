package com.fone.sms.data

data class AligoSmsRequest(
    val key: String,
    val userId: String,
    val sender: String,
    val receiver: String,
    val msg: String,
    val msgType: String = "SMS",
)
