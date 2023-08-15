package com.fone.profile.domain.service

import com.fone.common.entity.Type
import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.profile.domain.repository.ProfileCategoryRepository
import com.fone.profile.domain.repository.ProfileDomainRepository
import com.fone.profile.domain.repository.ProfileRepository
import com.fone.profile.domain.repository.ProfileWantRepository
import com.fone.profile.presentation.dto.RetrieveProfileWantDto.RetrieveProfileWantResponse
import kotlinx.coroutines.coroutineScope
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveProfileWantService(
    private val profileWantRepository: ProfileWantRepository,
    private val profileRepository: ProfileRepository,
    private val profileDomainRepository: ProfileDomainRepository,
    private val profileCategoryRepository: ProfileCategoryRepository,
    private val userRepository: UserCommonRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveProfileWant(
        pageable: Pageable,
        email: String,
        type: Type?,
    ): RetrieveProfileWantResponse {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        return coroutineScope {
            val profiles = profileRepository.findWantAllByUserId(pageable, userId, type).content
            val userProfileWants = profileWantRepository.findByUserId(userId)

            val profileIds = profiles.map { it.id!! }.toList()
            val profileDomains = profileDomainRepository.findByProfileIds(profileIds)
            val profileCategories = profileCategoryRepository.findByProfileIds(profileIds)

            RetrieveProfileWantResponse(
                profiles,
                userProfileWants,
                profileDomains,
                profileCategories,
                pageable
            )
        }
    }
}
