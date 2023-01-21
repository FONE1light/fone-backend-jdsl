package com.fone.filmone.application.job_opening

import com.fone.filmone.domain.job_opening.service.PutJobOpeningService
import com.fone.filmone.presentation.job_opening.RegisterJobOpeningDto
import org.springframework.stereotype.Service

@Service
class PutJobOpeningFacade(
    private val putJobOpeningService: PutJobOpeningService,
) {

    suspend fun putJobOpening(
        request: RegisterJobOpeningDto.RegisterJobOpeningRequest,
        email: String,
        jobOpeningId: Long
    ) = putJobOpeningService.putJobOpening(request, email, jobOpeningId)
}