package com.fone.profile.application

import org.springframework.stereotype.Service

@Service
class DeleteProfileFacade(
    private val deleteProfileService: com.fone.profile.domain.service.DeleteProfileService,
) {

    suspend fun deleteProfile(email: String, profileId: Long) = deleteProfileService.deleteProfile(email, profileId)
}
