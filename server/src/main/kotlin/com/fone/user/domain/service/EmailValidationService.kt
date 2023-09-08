package com.fone.user.domain.service

import com.fone.common.exception.InvalidTokenException
import com.fone.common.redis.RedisRepository
import com.fone.user.domain.repository.EmailRepository
import com.fone.user.domain.repository.generateRandomCode
import com.fone.user.infrastructure.EmailRepositoryImpl
import com.fone.user.presentation.dto.EmailValidationDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.ses.model.SendEmailRequest
import java.util.UUID
import java.util.concurrent.TimeUnit

@Service
class EmailValidationService(
    private val redisRepository: RedisRepository,
    private val emailRepository: EmailRepository,
    @Value("\${security.aws.senderEmail}") private val emailSource: String,
) {
    private val emailTemplate =
        EmailRepositoryImpl::class.java.classLoader.getResource("email-template.html")!!.readText()

    suspend fun sendValidationCode(
        request: EmailValidationDto.EmailSendRequest,
    ): EmailValidationDto.EmailSendResponse {
        return runCatching {
            val code = generateRandomCode()
            val email = request.email
            redisRepository.setValue("user:$email:emailCode", code, 10, TimeUnit.MINUTES)
            val message = SendEmailRequest.builder()
                .source(emailSource)
                .destination {
                    it.toAddresses(email)
                }
                .message {
                    it.subject {
                        it.data("인증번호입니다.")
                    }
                    it.body {
                        it.html {
                            it.data(emailTemplate.replace("AUTHENTICATION_CODE", code))
                        }
                    }
                }.build()
            emailRepository.sendEmail(message)
            EmailValidationDto.EmailSendResponse(EmailValidationDto.ResponseType.SUCCESS)
        }.onFailure {
            it.printStackTrace()
        }.getOrDefault(EmailValidationDto.EmailSendResponse(EmailValidationDto.ResponseType.FAILURE))
    }

    suspend fun validateCode(
        request: EmailValidationDto.EmailValidationRequest,
    ): EmailValidationDto.EmailValidationResponse {
        val email = request.email
        val code = redisRepository.getValue("user:$email:emailCode")
        if (code != request.code) {
            throw InvalidTokenException()
        }
        val token = (UUID.randomUUID().toString() + UUID.randomUUID().toString()).replace("-", "")
        redisRepository.setValue("user:$email:emailSignUpToken", token, 2, TimeUnit.HOURS)
        return EmailValidationDto.EmailValidationResponse(EmailValidationDto.ResponseType.SUCCESS, token)
    }
}
