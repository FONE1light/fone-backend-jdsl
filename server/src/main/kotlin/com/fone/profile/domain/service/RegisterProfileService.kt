package com.fone.profile.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.profile.domain.entity.ProfileCategory
import com.fone.profile.domain.entity.ProfileDomain
import com.fone.profile.domain.entity.ProfileImage
import com.fone.profile.domain.repository.ProfileCategoryRepository
import com.fone.profile.domain.repository.ProfileDomainRepository
import com.fone.profile.domain.repository.ProfileRepository
import com.fone.profile.domain.repository.ProfileWantRepository
import com.fone.profile.presentation.dto.RegisterProfileDto.RegisterProfileRequest
import com.fone.profile.presentation.dto.RegisterProfileDto.RegisterProfileResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterProfileService(
    private val profileRepository: ProfileRepository,
    private val profileWantRepository: ProfileWantRepository,
    private val profileDomainRepository: ProfileDomainRepository,
    private val profileCategoryRepository: ProfileCategoryRepository,
    private val userRepository: UserCommonRepository,
) {

    @Transactional
    suspend fun registerProfile(
        request: RegisterProfileRequest,
        email: String,
    ): RegisterProfileResponse {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()
        return with(request) {
            val profile = toEntity(userId)
            profileUrls.forEach {
                profile.addProfileImage(
                    ProfileImage(
                        it
                    )
                )
            }

            profileRepository.save(profile)

            val profileDomains = domains?.map {
                ProfileDomain(
                    profile.id!!,
                    it
                )
            }

            val profileCategories = categories.map {
                ProfileCategory(
                    profile.id!!,
                    it
                )
            }

            profileDomainRepository.saveAll(profileDomains)
            profileCategoryRepository.saveAll(profileCategories)

            val userProfileWants = profileWantRepository.findByUserId(userId)

            RegisterProfileResponse(
                profile,
                userProfileWants,
                domains,
                categories
            )
        }
    }
}
