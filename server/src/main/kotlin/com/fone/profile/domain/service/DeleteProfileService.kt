package com.fone.profile.domain.service

import com.fone.common.exception.InvalidProfileUserIdException
import com.fone.common.exception.NotFoundProfileException
import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.profile.domain.repository.ProfileCategoryRepository
import com.fone.profile.domain.repository.ProfileDomainRepository
import com.fone.profile.domain.repository.ProfileImageRepository
import com.fone.profile.domain.repository.ProfileRepository
import com.fone.profile.domain.repository.ProfileSnsRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class DeleteProfileService(
    private val profileRepository: ProfileRepository,
    private val profileDomainRepository: ProfileDomainRepository,
    private val profileCategoryRepository: ProfileCategoryRepository,
    private val userRepository: UserCommonRepository,
    private val profileSnsRepository: ProfileSnsRepository,
    private val profileImageRepository: ProfileImageRepository,
) {
    @Transactional
    suspend fun deleteProfile(
        email: String,
        profileId: Long,
    ) {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()
        val profile = profileRepository.findByTypeAndId(null, profileId) ?: throw NotFoundProfileException()

        if (profile.userId != userId) {
            throw InvalidProfileUserIdException()
        }
        profile.delete()
        profileRepository.save(profile)
        profileSnsRepository.deleteByProfileId(profileId)
        profileImageRepository.deleteByProfileId(profileId)
        profileDomainRepository.deleteByProfileId(profileId)
        profileCategoryRepository.deleteByProfileId(profileId)
    }
}
