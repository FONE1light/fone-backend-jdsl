package com.fone.filmone.application.job_opening

import com.fone.filmone.domain.job_opening.service.RegisterJobOpeningService
import com.fone.filmone.presentation.job_opening.RegisterJobOpeningDto
import org.springframework.stereotype.Service

@Service
class RegisterJobOpeningFacade(
    val registerJobOpeningService: RegisterJobOpeningService
) {

    suspend fun registerJobOpening(
        request: RegisterJobOpeningDto.RegisterJobOpeningRequest,
        email: String
    ) = registerJobOpeningService.registerJobOpening(request, email)
}