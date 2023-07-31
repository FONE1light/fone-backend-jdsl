package com.fone.user.domain.repository

import software.amazon.awssdk.services.ses.model.SendEmailRequest

interface EmailRepository : MessageRepository {
    suspend fun sendEmail(emailRequest: SendEmailRequest)
}
