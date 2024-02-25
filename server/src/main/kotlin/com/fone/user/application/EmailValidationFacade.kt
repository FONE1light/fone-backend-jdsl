package com.fone.user.application

import com.fone.user.presentation.dto.EmailDuplicationRequest
import com.fone.user.presentation.dto.EmailDuplicationResponse
import com.fone.user.presentation.dto.EmailSendRequest
import com.fone.user.presentation.dto.EmailValidationRequest
import com.fone.user.presentation.dto.EmailValidationResponse

interface EmailValidationFacade {

    suspend fun sendValidationMessage(
        emailSendRequest: EmailSendRequest,
    )

    suspend fun validateCode(
        emailValidationRequest: EmailValidationRequest,
    ): EmailValidationResponse

    suspend fun duplicateCheck(
        emailDuplicationRequest: EmailDuplicationRequest,
    ): EmailDuplicationResponse
}
