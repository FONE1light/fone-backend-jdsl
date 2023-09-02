package com.fone.user.infrastructure

import com.fone.sms.application.SmsFacade
import com.fone.sms.presentation.data.Result
import com.fone.sms.presentation.data.SMSVerificationRequest
import com.fone.user.domain.repository.SMSRepository
import org.springframework.stereotype.Service

@Service
class SMSRepositoryImpl(private val smsFacade: SmsFacade) : SMSRepository {

    override suspend fun sendValidationMessage(phone: String, code: String) {
        val response = smsFacade.sendSmsMessage(SMSVerificationRequest(phone, code))
        assert(response.result == Result.SUCCESS)
    }
}
