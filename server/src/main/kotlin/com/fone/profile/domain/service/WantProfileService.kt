package com.fone.profile.domain.service

import com.fone.common.exception.NotFoundProfileException
import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.profile.domain.entity.ProfileWant
import com.fone.profile.domain.repository.ProfileRepository
import com.fone.profile.domain.repository.ProfileWantRepository
import org.springframework.stereotype.Service

@Service
class WantProfileService(
    private val userRepository: UserCommonRepository,
    private val profileWantRepository: ProfileWantRepository,
    private val profileRepository: ProfileRepository,
) {

    suspend fun wantProfile(email: String, profileId: Long) {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        profileWantRepository.findByUserIdAndProfileId(userId, profileId)?.let {
            profileWantRepository.delete(it)

            val profile = profileRepository.findByTypeAndId(null, profileId) ?: throw NotFoundProfileException()

            profile.wantCount -= 1

            profileRepository.save(profile)
            return
        }

        val profile = profileRepository.findByTypeAndId(null, profileId) ?: throw NotFoundProfileException()

        profile.wantCount += 1

        profileWantRepository.save(ProfileWant(userId, profileId))
        profileRepository.save(profile)
    }
}
