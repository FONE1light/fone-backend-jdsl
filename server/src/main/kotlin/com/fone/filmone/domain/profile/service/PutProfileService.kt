package com.fone.filmone.domain.profile.service

import com.fone.common.exception.InvalidProfileUserIdException
import com.fone.common.exception.NotFoundProfileException
import com.fone.common.exception.NotFoundUserException
import com.fone.filmone.domain.profile.entity.ProfileCategory
import com.fone.filmone.domain.profile.entity.ProfileDomain
import com.fone.filmone.domain.profile.repository.ProfileCategoryRepository
import com.fone.filmone.domain.profile.repository.ProfileDomainRepository
import com.fone.filmone.domain.profile.repository.ProfileRepository
import com.fone.filmone.domain.profile.repository.ProfileWantRepository
import com.fone.user.domain.repository.UserRepository
import com.fone.filmone.presentation.profile.RegisterProfileDto.RegisterProfileRequest
import com.fone.filmone.presentation.profile.RegisterProfileDto.RegisterProfileResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
class PutProfileService(
    private val profileRepository: ProfileRepository,
    private val profileWantRepository: ProfileWantRepository,
    private val profileDomainRepository: ProfileDomainRepository,
    private val profileCategoryRepository: ProfileCategoryRepository,
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
            val profileDomains = async {
                profileDomainRepository.deleteByProfileId(profile.id!!)
                val profileDomains = request.domains.map {
                    ProfileDomain(
                        profile.id!!,
                        it
                    )
                }
                profileDomainRepository.saveAll(profileDomains)
            }

            val profileCategories = async {
                profileCategoryRepository.deleteByProfileId(profile.id!!)
                val profileCategories = request.categories.map {
                    ProfileCategory(
                        profile.id!!,
                        it
                    )
                }
                profileCategoryRepository.saveAll(profileCategories)
            }

            val profile = async {
                profile.put(request)
                profileRepository.save(profile)
            }

            val userProfileWants = async {
                profileWantRepository.findByUserId(user.id!!)
            }

            profileDomains.await()
            profileCategories.await()

            RegisterProfileResponse(
                profile.await(),
                userProfileWants.await(),
                request.domains,
                request.categories,
            )
        }
    }
}