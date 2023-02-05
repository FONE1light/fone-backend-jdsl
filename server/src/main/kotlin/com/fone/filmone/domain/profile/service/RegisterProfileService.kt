package com.fone.filmone.domain.profile.service

import com.fone.common.exception.NotFoundUserException
import com.fone.filmone.domain.profile.entity.ProfileCategory
import com.fone.filmone.domain.profile.entity.ProfileDomain
import com.fone.filmone.domain.profile.entity.ProfileImage
import com.fone.filmone.domain.profile.repository.ProfileCategoryRepository
import com.fone.filmone.domain.profile.repository.ProfileDomainRepository
import com.fone.filmone.domain.profile.repository.ProfileRepository
import com.fone.filmone.domain.profile.repository.ProfileWantRepository
import com.fone.filmone.domain.user.repository.UserRepository
import com.fone.filmone.presentation.profile.RegisterProfileDto.RegisterProfileRequest
import com.fone.filmone.presentation.profile.RegisterProfileDto.RegisterProfileResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()

        return coroutineScope {
            with(request) {
                val profile = async {
                    val profile = toEntity(user.id!!)
                    profileUrls.forEach { profile.addProfileImage(ProfileImage(it)) }

                    profileRepository.save(profile)

                    val profileDomains = domains.map {
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

                    profile
                }

                val userProfileWants = async {
                    profileWantRepository.findByUserId(user.id!!)
                }

                RegisterProfileResponse(
                    profile.await(),
                    userProfileWants.await(),
                    domains,
                    categories,
                )
            }
        }
    }
}