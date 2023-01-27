package com.fone.filmone.domain.profile.service

import com.fone.common.exception.InvalidProfileUserIdException
import com.fone.common.exception.NotFoundProfileException
import com.fone.common.exception.NotFoundUserException
import com.fone.filmone.domain.profile.repository.ProfileRepository
import com.fone.filmone.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class DeleteProfileService(
    private val profileRepository: ProfileRepository,
    private val userRepository: UserRepository,
) {

    suspend fun deleteProfile(email: String, profileId: Long) {
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()

        val profile = profileRepository.findByTypeAndId(null, profileId)
            ?: throw NotFoundProfileException()

        if (profile.userId != user.id) {
            throw InvalidProfileUserIdException()
        }

        profile.delete()

        profileRepository.save(profile)
    }
}