package com.fone.user.infrastructure

import com.fone.common.IntegrationTest
import com.fone.user.domain.repository.EmailRepository
import com.fone.user.domain.repository.generateRandomCode
import io.kotest.core.spec.style.ShouldSpec
import software.amazon.awssdk.services.ses.model.SendEmailRequest

@IntegrationTest
class EmailRepositoryTest(
    private val emailRepository: EmailRepository,
) : ShouldSpec({
    xshould("이메일을 정상적으로 보낸다") {
        val emailTemplate =
            EmailRepositoryTest::class.java.classLoader.getResource("email-template.html")!!.readText()
        val code = generateRandomCode()
        val email = "fyimbtmn@gmail.com"
        val message = SendEmailRequest.builder()
            .source("fyimbtmn@gmail.com")
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
    }
})
