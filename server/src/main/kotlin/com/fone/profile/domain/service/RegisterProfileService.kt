package com.fone.profile.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.profile.presentation.dto.RegisterProfileDto.RegisterProfileRequest
import com.fone.profile.presentation.dto.RegisterProfileDto.RegisterProfileResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterProfileService(
    private val profileRepository: com.fone.profile.domain.repository.ProfileRepository,
    private val profileWantRepository: com.fone.profile.domain.repository.ProfileWantRepository,
    private val profileDomainRepository: com.fone.profile.domain.repository.ProfileDomainRepository,
    private val profileCategoryRepository: com.fone.profile.domain.repository.ProfileCategoryRepository,
    private val userRepository: UserCommonRepository,
) {

    @Transactional
    suspend fun registerProfile(
        request: RegisterProfileRequest,
        email: String,
    ): RegisterProfileResponse {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        return coroutineScope {
            with(request) {
                val profile = async {
                    val profile = toEntity(userId)
                    profileUrls.forEach {
                        profile.addProfileImage(
                            com.fone.profile.domain.entity.ProfileImage(
                                it
                            )
                        )
                    }

                    profileRepository.save(profile)

                    val profileDomains = domains.map {
                        com.fone.profile.domain.entity.ProfileDomain(
                            profile.id!!,
                            it
                        )
                    }

                    val profileCategories = categories.map {
                        com.fone.profile.domain.entity.ProfileCategory(
                            profile.id!!,
                            it
                        )
                    }

                    profileDomainRepository.saveAll(profileDomains)
                    profileCategoryRepository.saveAll(profileCategories)

                    profile
                }

                val userProfileWants = async { profileWantRepository.findByUserId(userId) }

                RegisterProfileResponse(
                    profile.await(),
                    userProfileWants.await(),
                    domains,
                    categories
                )
            }
        }
    }
}
