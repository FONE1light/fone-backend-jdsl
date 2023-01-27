package com.fone.filmone.domain.profile.service

import com.fone.common.exception.InvalidProfileUserIdException
import com.fone.common.exception.NotFoundProfileException
import com.fone.common.exception.NotFoundUserException
import com.fone.filmone.domain.profile.repository.ProfileRepository
import com.fone.filmone.domain.profile.repository.ProfileWantRepository
import com.fone.filmone.domain.user.repository.UserRepository
import com.fone.filmone.presentation.profile.RegisterProfileDto.RegisterProfileRequest
import com.fone.filmone.presentation.profile.RegisterProfileDto.RegisterProfileResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
class PutProfileService(
    private val profileRepository: ProfileRepository,
    private val profileWantRepository: ProfileWantRepository,
    private val userRepository: UserRepository,
) {

    suspend fun putProfile(
        request: RegisterProfileRequest,
        email: String,
        profileId: Long,
    ): RegisterProfileResponse {
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()
        val profile = profileRepository.findByTypeAndId(null, profileId)
            ?: throw NotFoundProfileException()
        if (user.id != profile.userId) {
            throw InvalidProfileUserIdException()
        }

        return coroutineScope {
            val profile = async {
                profile.put(request)
                profileRepository.save(profile)
            }

            val userProfileWants = async {
                profileWantRepository.findByUserId(user.id!!)
            }

            RegisterProfileResponse(profile.await(), userProfileWants.await())
        }
    }
}