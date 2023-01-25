package com.fone.filmone.domain.profile.service

import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.domain.profile.entity.ProfileWant
import com.fone.filmone.domain.profile.repository.ProfileWantRepository
import com.fone.filmone.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class WantProfileService(
    private val userRepository: UserRepository,
    private val profileWantRepository: ProfileWantRepository,
) {

    suspend fun wantProfile(email: String, profileId: Long) {
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()

        profileWantRepository.findByUserIdAndProfileId(user.id!!, profileId)
            ?.let {
                profileWantRepository.delete(it)
                return
            }

        val profileWant = ProfileWant(user.id!!, profileId)
        profileWantRepository.save(profileWant)
    }
}