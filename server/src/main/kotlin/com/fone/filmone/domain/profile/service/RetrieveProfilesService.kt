package com.fone.filmone.domain.profile.service

import com.fone.filmone.infrastructure.profile.ProfileRepository
import com.fone.filmone.presentation.profile.RetrieveProfilesDto.RetrieveProfilesResponse
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.kotlin.core.publisher.toMono

@Service
class RetrieveProfilesService(
    private val profileRepository: ProfileRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveProfiles(pageable: Pageable): RetrieveProfilesResponse {
        val profiles = profileRepository.findBy(pageable)
            .map { it.toMono().awaitSingle() }
            .toList()

        return RetrieveProfilesResponse(profiles, pageable)
    }
}