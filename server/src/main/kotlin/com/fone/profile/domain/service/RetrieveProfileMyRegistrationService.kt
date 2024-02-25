package com.fone.profile.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.profile.domain.repository.ProfileCategoryRepository
import com.fone.profile.domain.repository.ProfileDomainRepository
import com.fone.profile.domain.repository.ProfileRepository
import com.fone.profile.domain.repository.ProfileWantRepository
import com.fone.profile.presentation.dto.RetrieveProfileMyRegistrationResponse
import com.fone.user.domain.repository.UserRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveProfileMyRegistrationService(
    private val userRepository: UserRepository,
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
        val user = userRepository.findByEmail(email) ?: throw NotFoundUserException()
        val userProfileWants = profileWantRepository.findByUserId(user.id!!)
        val profiles = profileRepository.findAllByUserId(pageable, user.id!!)
        val profileIds = profiles.map { it.id!! }.toList()
        val profileDomains = profileDomainRepository.findByProfileIds(profileIds)
        val profileCategories = profileCategoryRepository.findByProfileIds(profileIds)

        val profileUserIds = profiles.map { it.userId }.toList()
        val profileUsers = userRepository.findByIds(profileUserIds).associateBy { it.id }

        return RetrieveProfileMyRegistrationResponse(
            profiles,
            userProfileWants,
            profileDomains,
            profileCategories,
            profileUsers
        )
    }
}
