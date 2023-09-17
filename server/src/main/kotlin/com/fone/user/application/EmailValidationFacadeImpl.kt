package com.fone.user.application

import com.fone.user.domain.service.EmailValidationService
import com.fone.user.presentation.dto.EmailValidationDto
import org.springframework.stereotype.Service

@Service
class EmailValidationFacadeImpl(private val emailValidationService: EmailValidationService) : EmailValidationFacade {

    override suspend fun sendValidationMessage(emailSendRequest: EmailValidationDto.EmailSendRequest) =
        emailValidationService.sendValidationCode(emailSendRequest)

    override suspend fun validateCode(emailValidationRequest: EmailValidationDto.EmailValidationRequest) =
        emailValidationService.validateCode(emailValidationRequest)

    override suspend fun duplicateCheck(emailDuplicationRequest: EmailValidationDto.EmailDuplicationRequest) =
        emailValidationService.checkDuplicate(emailDuplicationRequest)
}
