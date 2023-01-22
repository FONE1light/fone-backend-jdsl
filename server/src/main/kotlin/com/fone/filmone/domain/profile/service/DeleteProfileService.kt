package com.fone.filmone.domain.profile.service

import com.fone.filmone.common.exception.InvalidProfileUserIdException
import com.fone.filmone.common.exception.NotFoundProfileException
import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.infrastructure.profile.ProfileRepository
import com.fone.filmone.infrastructure.user.UserRepository
import org.springframework.stereotype.Service

@Service
class DeleteProfileService(
    private val profileRepository: ProfileRepository,
    private val userRepository: UserRepository,
) {

    suspend fun deleteProfile(email: String, profileId: Long) {
        val user = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        val profile = profileRepository.findById(profileId)
            ?: throw NotFoundProfileException()

        if (profile.userId != user.id) {
            throw InvalidProfileUserIdException()
        }

        profile.delete()

        profileRepository.save(profile)
    }
}