package com.fone.profile.domain.service

import com.fone.common.exception.InvalidProfileUserIdException
import com.fone.common.exception.NotFoundProfileException
import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.profile.domain.entity.ProfileCategory
import com.fone.profile.domain.entity.ProfileDomain
import com.fone.profile.domain.repository.ProfileCategoryRepository
import com.fone.profile.domain.repository.ProfileDomainRepository
import com.fone.profile.domain.repository.ProfileRepository
import com.fone.profile.domain.repository.ProfileWantRepository
import com.fone.profile.presentation.dto.RegisterProfileDto.RegisterProfileRequest
import com.fone.profile.presentation.dto.RegisterProfileDto.RegisterProfileResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
class PutProfileService(
    private val profileRepository: ProfileRepository,
    private val profileWantRepository: ProfileWantRepository,
    private val profileDomainRepository: ProfileDomainRepository,
    private val profileCategoryRepository: ProfileCategoryRepository,
    private val userRepository: UserCommonRepository,
) {

    suspend fun putProfile(
        request: RegisterProfileRequest,
        email: String,
        profileId: Long,
    ): RegisterProfileResponse {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()
        val profile = profileRepository.findByTypeAndId(null, profileId)
            ?: throw NotFoundProfileException()
        if (userId != profile.userId) {
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
                profileWantRepository.findByUserId(userId)
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