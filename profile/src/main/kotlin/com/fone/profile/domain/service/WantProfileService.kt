package com.fone.profile.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.profile.domain.entity.ProfileWant
import com.fone.profile.domain.repository.ProfileWantRepository
import org.springframework.stereotype.Service

@Service
class WantProfileService(
    private val userRepository: UserCommonRepository,
    private val profileWantRepository: ProfileWantRepository,
) {

    suspend fun wantProfile(email: String, profileId: Long) {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        profileWantRepository.findByUserIdAndProfileId(userId, profileId)
            ?.let {
                profileWantRepository.delete(it)
                return
            }

        val profileWant = ProfileWant(userId, profileId)
        profileWantRepository.save(profileWant)
    }
}