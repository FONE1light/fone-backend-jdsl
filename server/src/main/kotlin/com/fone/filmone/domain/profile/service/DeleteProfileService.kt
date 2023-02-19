package com.fone.filmone.domain.profile.service

import com.fone.common.exception.InvalidProfileUserIdException
import com.fone.common.exception.NotFoundProfileException
import com.fone.common.exception.NotFoundUserException
import com.fone.filmone.domain.profile.repository.ProfileCategoryRepository
import com.fone.filmone.domain.profile.repository.ProfileDomainRepository
import com.fone.filmone.domain.profile.repository.ProfileRepository
import com.fone.user.domain.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
class DeleteProfileService(
    private val profileRepository: ProfileRepository,
    private val profileDomainRepository: ProfileDomainRepository,
    private val profileCategoryRepository: ProfileCategoryRepository,
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

        coroutineScope {
            val profile = async {
                profileRepository.save(profile)
            }
            val profileDomain = async {
                profileDomainRepository.deleteByProfileId(profileId)
            }
            val profileCategory = async {
                profileCategoryRepository.deleteByProfileId(profileId)
            }

            profile.await()
            profileDomain.await()
            profileCategory.await()
        }
    }
}