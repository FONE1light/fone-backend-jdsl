package com.fone.profile.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.profile.domain.entity.ProfileCategory
import com.fone.profile.domain.entity.ProfileDomain
import com.fone.profile.domain.entity.ProfileImage
import com.fone.profile.domain.repository.ProfileCategoryRepository
import com.fone.profile.domain.repository.ProfileDomainRepository
import com.fone.profile.domain.repository.ProfileRepository
import com.fone.profile.domain.repository.ProfileWantRepository
import com.fone.profile.presentation.dto.RegisterProfileDto.RegisterProfileRequest
import com.fone.profile.presentation.dto.RegisterProfileDto.RegisterProfileResponse
import com.fone.user.domain.enum.Job
import com.fone.user.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterProfileService(
    private val profileRepository: ProfileRepository,
    private val profileWantRepository: ProfileWantRepository,
    private val profileDomainRepository: ProfileDomainRepository,
    private val profileCategoryRepository: ProfileCategoryRepository,
    private val userRepository: UserRepository,
) {

    @Transactional
    suspend fun registerProfile(
        request: RegisterProfileRequest,
        email: String,
    ): RegisterProfileResponse {
        val user = userRepository.findByEmail(email) ?: throw NotFoundUserException()
        return with(request) {
            val profile = toEntity(user.id!!)
            secondPage.profileImages.forEach {
                profile.addProfileImage(
                    ProfileImage(
                        it
                    )
                )
            }

            profileRepository.save(profile)

            val profileDomains = thirdPage.domains?.map {
                ProfileDomain(
                    profile.id!!,
                    it
                )
            }

            val profileCategories = sixthPage.categories.map {
                ProfileCategory(
                    profile.id!!,
                    it
                )
            }

            profileDomainRepository.saveAll(profileDomains)
            profileCategoryRepository.saveAll(profileCategories)

            val userProfileWants = profileWantRepository.findByUserId(user.id!!)

            val profileUser = userRepository.findById(user.id!!)

            RegisterProfileResponse(
                profile,
                userProfileWants,
                thirdPage.domains,
                sixthPage.categories,
                profileUser?.nickname ?: "",
                profileUser?.profileUrl ?: "",
                profileUser?.job ?: Job.ACTOR
            )
        }
    }
}
