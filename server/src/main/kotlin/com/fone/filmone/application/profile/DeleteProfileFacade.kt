package com.fone.filmone.application.profile

import com.fone.filmone.domain.profile.service.DeleteProfileService
import org.springframework.stereotype.Service

@Service
class DeleteProfileFacade(
    private val deleteProfileService: DeleteProfileService,
) {

    suspend fun deleteProfile(email: String, profileId: Long) =
        deleteProfileService.deleteProfile(email, profileId)
}