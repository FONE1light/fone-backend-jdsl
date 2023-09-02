package com.fone.sms.presentation.data

data class SmsVerificationResponse(
    val result: Result,
    val data: SmsResponseData,
    val message: String,
    val errorCode: String? = null,
)

data class SmsResponseData(val messageId: String)

enum class Result {
    SUCCESS, FAILURE
}
