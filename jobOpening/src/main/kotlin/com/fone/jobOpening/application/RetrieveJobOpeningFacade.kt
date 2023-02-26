package com.fone.jobOpening.application

import com.fone.common.entity.Type
import com.fone.jobOpening.domain.service.RetrieveJobOpeningService
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningDto.RetrieveJobOpeningsRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RetrieveJobOpeningFacade(
    private val retrieveJobOpeningService: RetrieveJobOpeningService,
) {

    suspend fun retrieveJobOpenings(
        email: String,
        pageable: Pageable,
        request: RetrieveJobOpeningsRequest,
    ) = retrieveJobOpeningService.retrieveJobOpenings(email, pageable, request)

    suspend fun retrieveJobOpening(email: String, type: Type, jobOpeningId: Long) =
        retrieveJobOpeningService.retrieveJobOpening(email, type, jobOpeningId)
}
