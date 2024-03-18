package com.fone.user.application

import com.fone.user.domain.service.EmailValidationService
import com.fone.user.presentation.dto.EmailDuplicationRequest
import com.fone.user.presentation.dto.EmailSendRequest
import com.fone.user.presentation.dto.EmailValidationRequest
import com.fone.user.presentation.dto.EmailValidationResponse
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Service
@Primary
class EmailValidationFacadeNoOp(private val emailValidationService: EmailValidationService) : EmailValidationFacade {
    override suspend fun sendValidationMessage(emailSendRequest: EmailSendRequest) = Unit

    override suspend fun validateCode(emailValidationRequest: EmailValidationRequest) = EmailValidationResponse("NO-OP")

    override suspend fun duplicateCheck(emailDuplicationRequest: EmailDuplicationRequest) =
        emailValidationService.checkDuplicate(emailDuplicationRequest)
}
