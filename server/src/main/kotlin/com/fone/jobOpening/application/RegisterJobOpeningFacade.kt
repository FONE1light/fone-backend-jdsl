package com.fone.jobOpening.application

import com.fone.jobOpening.domain.service.RegisterJobOpeningService
import com.fone.jobOpening.presentation.dto.RegisterJobOpeningRequest
import org.springframework.stereotype.Service

@Service
class RegisterJobOpeningFacade(
    val registerJobOpeningService: RegisterJobOpeningService,
) {

    suspend fun registerJobOpening(
        request: RegisterJobOpeningRequest,
        email: String,
    ) = registerJobOpeningService.registerJobOpening(request, email)
}
