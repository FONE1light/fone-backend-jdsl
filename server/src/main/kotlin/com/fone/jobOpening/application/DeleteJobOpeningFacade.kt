package com.fone.jobOpening.application

import org.springframework.stereotype.Service

@Service
class DeleteJobOpeningFacade(
    private val deleteJobOpeningService: com.fone.jobOpening.domain.service.DeleteJobOpeningService,
) {

    suspend fun deleteJobOpening(email: String, jobOpeningId: Long) =
        deleteJobOpeningService.deleteJobOpening(email, jobOpeningId)
}
