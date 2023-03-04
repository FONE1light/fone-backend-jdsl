package com.fone.profile.application

import com.fone.profile.domain.service.DeleteProfileService
import org.springframework.stereotype.Service

@Service
class DeleteProfileFacade(
    private val deleteProfileService: DeleteProfileService
) {

    suspend fun deleteProfile(email: String, profileId: Long) =
        deleteProfileService.deleteProfile(email, profileId)
}
