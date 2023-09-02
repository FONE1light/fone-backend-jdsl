package com.fone.sms.domain.data

data class AligoSmsRequest(
    var key: String = "",
    var userId: String = "",
    var sender: String = "",
    val receiver: String,
    val msg: String,
    val msgType: String = "SMS",
)
