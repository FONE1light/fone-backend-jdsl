package com.fone.profile.domain.service

import com.fone.common.exception.NotFoundProfileException
import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import org.springframework.stereotype.Service

@Service
class WantProfileService(
    private val userRepository: UserCommonRepository,
    private val profileWantRepository: com.fone.profile.domain.repository.ProfileWantRepository,
    private val profileRepository: com.fone.profile.domain.repository.ProfileRepository,
) {

    suspend fun wantProfile(email: String, profileId: Long) {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        profileWantRepository.findByUserIdAndProfileId(userId, profileId)?.let {
            profileWantRepository.delete(it)
            return
        }

        profileRepository.findByTypeAndId(null, profileId) ?: throw NotFoundProfileException()

        profileWantRepository.save(com.fone.profile.domain.entity.ProfileWant(userId, profileId))
    }
}
