package com.fone.user.application

import com.fone.user.domain.service.EmailValidationService
import com.fone.user.presentation.dto.EmailValidationDto
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service

@Service
@Primary
class EmailValidationFacadeNoOp(private val emailValidationService: EmailValidationService) : EmailValidationFacade {

    override suspend fun sendValidationMessage(emailSendRequest: EmailValidationDto.EmailSendRequest) =
        EmailValidationDto.EmailSendResponse(EmailValidationDto.ResponseType.SUCCESS)

    override suspend fun validateCode(emailValidationRequest: EmailValidationDto.EmailValidationRequest) =
        EmailValidationDto.EmailValidationResponse(EmailValidationDto.ResponseType.SUCCESS, "NO-OP")

    override suspend fun duplicateCheck(emailDuplicationRequest: EmailValidationDto.EmailDuplicationRequest) =
        emailValidationService.checkDuplicate(emailDuplicationRequest)
}
