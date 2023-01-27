package com.fone.filmone.domain.profile.service

import com.fone.filmone.common.exception.NotFoundProfileException
import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.profile.repository.ProfileRepository
import com.fone.filmone.domain.profile.repository.ProfileWantRepository
import com.fone.filmone.domain.user.repository.UserRepository
import com.fone.filmone.presentation.profile.RetrieveProfilesDto.RetrieveProfileResponse
import com.fone.filmone.presentation.profile.RetrieveProfilesDto.RetrieveProfilesResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveProfilesService(
    private val profileRepository: ProfileRepository,
    private val profileWantRepository: ProfileWantRepository,
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveProfiles(
        pageable: Pageable,
        email: String,
        type: Type,
    ): RetrieveProfilesResponse {
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()

        return coroutineScope {
            val profiles = async {
                profileRepository.findAllByType(pageable, type).content
            }

            val userProfileWants = async {
                profileWantRepository.findByUserId(user.id!!)
            }

            RetrieveProfilesResponse(profiles.await(), userProfileWants.await(), pageable)
        }
    }

    @Transactional
    suspend fun retrieveProfile(
        email: String,
        type: Type,
        profileId: Long,
    ): RetrieveProfileResponse {
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()

        return coroutineScope {
            val profile = async {
                val profile = profileRepository.findByTypeAndId(type, profileId)
                    ?: throw NotFoundProfileException()
                profile.view()
                profileRepository.save(profile)
            }

            val userProfileWants = async {
                profileWantRepository.findByUserId(user.id!!)
            }

            RetrieveProfileResponse(profile.await(), userProfileWants.await())
        }
    }
}