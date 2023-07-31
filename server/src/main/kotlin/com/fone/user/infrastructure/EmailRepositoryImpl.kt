package com.fone.user.infrastructure

import com.fone.user.domain.repository.EmailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Repository
import software.amazon.awssdk.services.ses.SesClient
import software.amazon.awssdk.services.ses.model.SendEmailRequest

@Repository
class EmailRepositoryImpl(private val sesClient: SesClient) : EmailRepository {
    override suspend fun sendEmail(emailRequest: SendEmailRequest) {
        withContext(Dispatchers.IO) {
            sesClient.sendEmail(emailRequest)
        }
    }
}
