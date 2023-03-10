package com.fone.jobOpening.application

import com.fone.jobOpening.domain.service.DeleteJobOpeningService
import org.springframework.stereotype.Service

@Service
class DeleteJobOpeningFacade(
    private val deleteJobOpeningService: DeleteJobOpeningService,
) {

    suspend fun deleteJobOpening(email: String, jobOpeningId: Long) =
        deleteJobOpeningService.deleteJobOpening(email, jobOpeningId)
}
