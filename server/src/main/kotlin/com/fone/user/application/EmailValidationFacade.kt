package com.fone.user.application

import com.fone.user.presentation.dto.EmailValidationDto

interface EmailValidationFacade {

    suspend fun sendValidationMessage(emailSendRequest: EmailValidationDto.EmailSendRequest): EmailValidationDto.EmailSendResponse

    suspend fun validateCode(emailValidationRequest: EmailValidationDto.EmailValidationRequest): EmailValidationDto.EmailValidationResponse
}
