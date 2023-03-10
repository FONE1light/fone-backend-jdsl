package com.fone.profile.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.profile.domain.repository.ProfileCategoryRepository
import com.fone.profile.domain.repository.ProfileDomainRepository
import com.fone.profile.domain.repository.ProfileRepository
import com.fone.profile.domain.repository.ProfileWantRepository
import com.fone.profile.presentation.dto.RetrieveProfileMyRegistrationDto.RetrieveProfileMyRegistrationResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveProfileMyRegistrationService(
    private val userRepository: UserCommonRepository,
    private val profileWantRepository: ProfileWantRepository,
    private val profileRepository: ProfileRepository,
    private val profileDomainRepository: ProfileDomainRepository,
    private val profileCategoryRepository: ProfileCategoryRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveProfileMyRegistration(
        pageable: Pageable,
        email: String,
    ): RetrieveProfileMyRegistrationResponse {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        return coroutineScope {
            val userProfileWants = async { profileWantRepository.findByUserId(userId) }

            val profiles = profileRepository.findAllByUserId(pageable, userId).content
            val profileIds = profiles.map { it.id!! }.toList()
            val profileDomains = profileDomainRepository.findByProfileIds(profileIds)
            val profileCategories = profileCategoryRepository.findByProfileIds(profileIds)

            RetrieveProfileMyRegistrationResponse(
                profiles,
                userProfileWants.await(),
                profileDomains,
                profileCategories,
                pageable
            )
        }
    }
}
