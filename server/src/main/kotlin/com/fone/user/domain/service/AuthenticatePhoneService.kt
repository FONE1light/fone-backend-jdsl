package com.fone.user.domain.service

import com.fone.user.domain.repository.SMSRepository
import org.springframework.stereotype.Service

@Service
class AuthenticatePhoneService(
    private val smsRepository: SMSRepository,
) {
    private val phoneRegex = Regex("^(?:0|\\+82)1[0-9]-?\\d{4}-?\\d{4}\$")
    suspend fun sendMessageToPhone(phone: String, message: String) {
        assert(phoneRegex.matches(phone))
        smsRepository.sendValidationMessage(formatNumber(phone), message)
    }

    private fun formatNumber(phone: String): String {
        var phoneNumber = phone
        if (phoneNumber.startsWith("0")) {
            phoneNumber = phoneNumber.replaceFirst("0", "+82")
        }
        return phoneNumber.replace("-", "")
    }
}
