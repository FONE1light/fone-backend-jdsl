package com.fone.profile.application

import com.fone.common.entity.Type
import com.fone.profile.domain.service.RetrieveProfilesService
import com.fone.profile.presentation.dto.RetrieveProfilesDto.RetrieveProfilesRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RetrieveProfilesFacade(
    private val retrieveProfilesService: RetrieveProfilesService,
) {

    suspend fun retrieveProfiles(
        pageable: Pageable,
        email: String,
        request: RetrieveProfilesRequest,
    ) = retrieveProfilesService.retrieveProfiles(pageable, email, request)

    suspend fun retrieveProfile(email: String, type: Type, profileId: Long) =
        retrieveProfilesService.retrieveProfile(email, type, profileId)
}
