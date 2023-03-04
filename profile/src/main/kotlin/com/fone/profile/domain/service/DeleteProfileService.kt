package com.fone.profile.domain.service

import com.fone.common.exception.InvalidProfileUserIdException
import com.fone.common.exception.NotFoundProfileException
import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.profile.domain.repository.ProfileCategoryRepository
import com.fone.profile.domain.repository.ProfileDomainRepository
import com.fone.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
class DeleteProfileService(
    private val profileRepository: ProfileRepository,
    private val profileDomainRepository: ProfileDomainRepository,
    private val profileCategoryRepository: ProfileCategoryRepository,
    private val userRepository: UserCommonRepository
) {

    suspend fun deleteProfile(email: String, profileId: Long) {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()
        val profile =
            profileRepository.findByTypeAndId(null, profileId) ?: throw NotFoundProfileException()

        if (profile.userId != userId) {
            throw InvalidProfileUserIdException()
        }

        profile.delete()

        coroutineScope {
            val p = async { profileRepository.save(profile) }
            val profileDomain = async { profileDomainRepository.deleteByProfileId(profileId) }
            val profileCategory = async { profileCategoryRepository.deleteByProfileId(profileId) }

            p.await()
            profileDomain.await()
            profileCategory.await()
        }
    }
}
