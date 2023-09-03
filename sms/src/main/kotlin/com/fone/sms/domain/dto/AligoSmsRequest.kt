package com.fone.sms.domain.dto

import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

data class AligoSmsRequest(
    var key: String = "",
    var userId: String = "",
    var sender: String = "",
    val receiver: String,
    val msg: String,
    val msgType: String = "SMS",
)

fun AligoSmsRequest.toMap(): MultiValueMap<String, String> {
    val map = LinkedMultiValueMap<String, String>()
    map.add("key", key)
    map.add("user_id", userId)
    map.add("sender", sender)
    map.add("receiver", receiver)
    map.add("msg", msg)
    map.add("msg_type", msgType)
    return map
}
