package com.fone.filmone.domain.profile.service

import com.fone.filmone.common.exception.InvalidProfileUserIdException
import com.fone.filmone.common.exception.NotFoundProfileException
import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.infrastructure.profile.ProfileRepository
import com.fone.filmone.infrastructure.user.UserRepository
import com.fone.filmone.presentation.profile.RegisterProfileDto.*
import org.springframework.stereotype.Service

@Service
class PutProfileService(
    private val profileRepository: ProfileRepository,
    private val userRepository: UserRepository,
) {

    suspend fun putProfile(
        request: RegisterProfileRequest,
        email: String,
        profileId: Long,
    ): RegisterProfileResponse {
        val user = userRepository.findByEmail(email) ?: throw NotFoundUserException()
        val profile = profileRepository.findById(profileId)
            ?: throw NotFoundProfileException()
        if (user.id != profile.userId) {
            throw InvalidProfileUserIdException()
        }

        profile.put(request)

        profileRepository.save(profile)

        return RegisterProfileResponse(profile)
    }
}