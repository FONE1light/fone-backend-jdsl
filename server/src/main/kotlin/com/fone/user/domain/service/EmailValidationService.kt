package com.fone.user.domain.service

import com.fone.common.exception.DuplicateUserException
import com.fone.common.exception.EmailBackendException
import com.fone.common.exception.InvalidTokenException
import com.fone.common.redis.RedisRepository
import com.fone.user.domain.repository.EmailRepository
import com.fone.user.domain.repository.UserRepository
import com.fone.user.domain.repository.generateRandomCode
import com.fone.user.infrastructure.EmailRepositoryImpl
import com.fone.user.presentation.dto.EmailDuplicationRequest
import com.fone.user.presentation.dto.EmailDuplicationResponse
import com.fone.user.presentation.dto.EmailSendRequest
import com.fone.user.presentation.dto.EmailValidationRequest
import com.fone.user.presentation.dto.EmailValidationResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.ses.model.SendEmailRequest
import java.util.UUID
import java.util.concurrent.TimeUnit

@Service
class EmailValidationService(
    private val redisRepository: RedisRepository,
    private val emailRepository: EmailRepository,
    private val userRepository: UserRepository,
    @Value("\${security.aws.senderEmail}") private val emailSource: String,
) {
    private val emailTemplate =
        EmailRepositoryImpl::class.java.classLoader.getResource("email-template.html")!!.readText()

    suspend fun sendValidationCode(
        request: EmailSendRequest,
    ) {
        runCatching {
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
                        it.data("에프원 회원가입 인증번호입니다.")
                    }
                    it.body {
                        it.html {
                            it.data(emailTemplate.replace("AUTHENTICATION_CODE", code))
                        }
                    }
                }.build()
            emailRepository.sendEmail(message)
        }.onFailure {
            it.printStackTrace()
            throw EmailBackendException()
        }
    }

    suspend fun validateCode(
        request: EmailValidationRequest,
    ): EmailValidationResponse {
        val email = request.email
        val code = redisRepository.getValue("user:$email:emailCode")
        if (code != request.code) {
            throw InvalidTokenException()
        }
        redisRepository.delValue("user:$email:emailCode")
        val token = (UUID.randomUUID().toString() + UUID.randomUUID().toString()).replace("-", "")
        redisRepository.setValue("user:$email:emailSignUpToken", token, 2, TimeUnit.HOURS)
        return EmailValidationResponse(token)
    }

    suspend fun checkDuplicate(
        request: EmailDuplicationRequest,
    ): EmailDuplicationResponse {
        if (userRepository.findByEmail(request.email) != null) {
            throw DuplicateUserException()
        }
        return EmailDuplicationResponse(request.email)
    }
}
