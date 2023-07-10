package com.fone.profile.domain.service

import com.fone.common.entity.Type
import com.fone.common.exception.NotFoundProfileException
import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.profile.domain.repository.ProfileCategoryRepository
import com.fone.profile.domain.repository.ProfileDomainRepository
import com.fone.profile.domain.repository.ProfileRepository
import com.fone.profile.domain.repository.ProfileWantRepository
import com.fone.profile.presentation.dto.RetrieveProfilesDto.RetrieveProfileResponse
import com.fone.profile.presentation.dto.RetrieveProfilesDto.RetrieveProfilesRequest
import com.fone.profile.presentation.dto.RetrieveProfilesDto.RetrieveProfilesResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveProfilesService(
    private val profileRepository: ProfileRepository,
    private val profileWantRepository: ProfileWantRepository,
    private val userRepository: UserCommonRepository,
    private val profileDomainRepository: ProfileDomainRepository,
    private val profileCategoryRepository: ProfileCategoryRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveProfiles(
        pageable: Pageable,
        email: String,
        request: RetrieveProfilesRequest,
    ): RetrieveProfilesResponse {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        val profiles = profileRepository.findAllByFilters(pageable, request)
        val userProfileWants = profileWantRepository.findByUserId(userId)

        val profileIds = profiles.map { it.id!! }.toList()
        val profileDomains = profileDomainRepository.findByProfileIds(profileIds)
        val profileCategories = profileCategoryRepository.findByProfileIds(profileIds)

        return RetrieveProfilesResponse(
            profiles,
            userProfileWants,
            profileDomains,
            profileCategories
        )
    }

    @Transactional
    suspend fun retrieveProfile(
        email: String,
        type: Type,
        profileId: Long,
    ): RetrieveProfileResponse {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()
        val userNickname = userRepository.findNicknameByEmail(email) ?: throw NotFoundUserException()

        return coroutineScope {
            val profile = async {
                val profile = profileRepository.findByTypeAndId(type, profileId) ?: throw NotFoundProfileException()
                profile.view()
                profileRepository.save(profile)
            }

            val userProfileWants = async { profileWantRepository.findByUserId(userId) }

            val id = profile.await().id!!

            val profileDomains = async { profileDomainRepository.findByProfileId(id) }

            val profileCategories = async { profileCategoryRepository.findByProfileId(id) }

            RetrieveProfileResponse(
                profile.await(),
                userProfileWants.await(),
                profileDomains.await(),
                profileCategories.await(),
                userNickname
            )
        }
    }
}
