package com.fone.user.application

import com.fone.user.domain.service.EmailValidationService
import com.fone.user.presentation.dto.EmailDuplicationRequest
import com.fone.user.presentation.dto.EmailSendRequest
import com.fone.user.presentation.dto.EmailValidationRequest
import org.springframework.stereotype.Service

@Service
class EmailValidationFacadeImpl(private val emailValidationService: EmailValidationService) : EmailValidationFacade {

    override suspend fun sendValidationMessage(emailSendRequest: EmailSendRequest) =
        emailValidationService.sendValidationCode(emailSendRequest)

    override suspend fun validateCode(emailValidationRequest: EmailValidationRequest) =
        emailValidationService.validateCode(emailValidationRequest)

    override suspend fun duplicateCheck(emailDuplicationRequest: EmailDuplicationRequest) =
        emailValidationService.checkDuplicate(emailDuplicationRequest)
}
