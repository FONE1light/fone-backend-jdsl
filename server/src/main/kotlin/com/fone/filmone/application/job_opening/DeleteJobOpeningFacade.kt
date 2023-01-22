package com.fone.filmone.application.job_opening

import com.fone.filmone.domain.job_opening.service.DeleteJobOpeningService
import org.springframework.stereotype.Service

@Service
class DeleteJobOpeningFacade(
    private val deleteJobOpeningService: DeleteJobOpeningService,
) {

    suspend fun deleteJobOpening(email: String, jobOpeningId: Long) =
        deleteJobOpeningService.deleteJobOpening(email, jobOpeningId)
}