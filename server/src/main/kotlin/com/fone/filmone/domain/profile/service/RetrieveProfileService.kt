package com.fone.filmone.domain.profile.service

import com.fone.filmone.infrastructure.profile.ProfileRepository
import com.fone.filmone.presentation.profile.RetrieveProfileDto.RetrieveProfileResponse
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import reactor.kotlin.core.publisher.toMono

@Service
class RetrieveProfileService(
    private val profileRepository: ProfileRepository,
) {

    suspend fun retrieveProfile(pageable: Pageable): RetrieveProfileResponse {
        val profiles = profileRepository.findBy(pageable)
            .map { it.toMono().awaitSingle() }
            .toList()

        return RetrieveProfileResponse(profiles, pageable)
    }
}