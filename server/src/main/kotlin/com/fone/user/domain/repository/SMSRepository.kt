package com.fone.user.domain.repository

interface SMSRepository {
    suspend fun sendValidationMessage(phone: String, code: String)

    fun generateRandomCode(): String

    data class SMSRequest(
        val phone: String,
        val code: String,
    )
}
