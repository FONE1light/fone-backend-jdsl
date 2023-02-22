package com.fone.jobOpening.application

import com.fone.jobOpening.domain.service.PutJobOpeningService
import com.fone.jobOpening.presentation.dto.RegisterJobOpeningDto.RegisterJobOpeningRequest
import org.springframework.stereotype.Service

@Service
class PutJobOpeningFacade(
    private val putJobOpeningService: PutJobOpeningService,
) {

    suspend fun putJobOpening(
        request: RegisterJobOpeningRequest,
        email: String,
        jobOpeningId: Long,
    ) = putJobOpeningService.putJobOpening(request, email, jobOpeningId)
}