package com.fone.user.domain.repository

interface SMSRepository : MessageRepository{
    suspend fun sendValidationMessage(phone: String, code: String)

    data class SMSRequest(
        val phone: String,
        val code: String,
    )
}
