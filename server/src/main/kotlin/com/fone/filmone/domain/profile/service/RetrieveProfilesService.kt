package com.fone.filmone.domain.profile.service

import com.fone.filmone.common.exception.NotFoundProfileException
import com.fone.filmone.domain.common.Type
import com.fone.filmone.infrastructure.profile.ProfileRepository
import com.fone.filmone.presentation.profile.RetrieveProfilesDto.RetrieveProfileResponse
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
    suspend fun retrieveProfiles(pageable: Pageable, type: Type): RetrieveProfilesResponse {
        val profiles = profileRepository.findByType(pageable, type.toString())
            .map { it.toMono().awaitSingle() }
            .toList()

        return RetrieveProfilesResponse(profiles, pageable)
    }

    @Transactional
    suspend fun retrieveProfile(
        email: String,
        type: Type,
        profileId: Long,
    ): RetrieveProfileResponse {
        val profile =
            profileRepository.findByType(type.toString()) ?: throw NotFoundProfileException()
        profile.view()
        profileRepository.save(profile)

        return RetrieveProfileResponse(profile)
    }
}