package com.fone.profile.domain.service

import com.fone.common.exception.InvalidProfileUserIdException
import com.fone.common.exception.NotFoundProfileException
import com.fone.common.exception.NotFoundUserException
import com.fone.profile.domain.entity.ProfileCategory
import com.fone.profile.domain.entity.ProfileDomain
import com.fone.profile.domain.repository.ProfileCategoryRepository
import com.fone.profile.domain.repository.ProfileDomainRepository
import com.fone.profile.domain.repository.ProfileRepository
import com.fone.profile.domain.repository.ProfileWantRepository
import com.fone.profile.presentation.dto.RegisterProfileRequest
import com.fone.profile.presentation.dto.RegisterProfileResponse
import com.fone.user.domain.enum.Job
import com.fone.user.domain.repository.UserRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class PutProfileService(
    private val profileRepository: ProfileRepository,
    private val profileWantRepository: ProfileWantRepository,
    private val profileDomainRepository: ProfileDomainRepository,
    private val profileCategoryRepository: ProfileCategoryRepository,
    private val userRepository: UserRepository,
) {

    @Transactional
    suspend fun putProfile(
        request: RegisterProfileRequest,
        email: String,
        profileId: Long,
    ): RegisterProfileResponse {
        val user = userRepository.findByEmail(email) ?: throw NotFoundUserException()
        val profile = profileRepository.findByTypeAndId(null, profileId) ?: throw NotFoundProfileException()
        if (user.id != profile.userId) {
            throw InvalidProfileUserIdException()
        }
        profile.put(request)
        profileRepository.save(profile)

        val userProfileWants = profileWantRepository.findByUserId(user.id!!)

        profileDomainRepository.deleteByProfileId(profile.id!!)
        val profileDomains = request.thirdPage.domains?.map {
            ProfileDomain(
                profile.id!!,
                it
            )
        }
        profileDomainRepository.saveAll(profileDomains)

        profileCategoryRepository.deleteByProfileId(profile.id!!)
        val profileCategories = request.sixthPage.categories.map {
            ProfileCategory(
                profile.id!!,
                it
            )
        }
        profileCategoryRepository.saveAll(profileCategories)

        val profileUser = userRepository.findById(user.id!!)

        return RegisterProfileResponse(
            profile,
            userProfileWants,
            request.thirdPage.domains,
            request.sixthPage.categories,
            profileUser?.nickname ?: "",
            profileUser?.profileUrl ?: "",
            profileUser?.job ?: Job.ACTOR
        )
    }
}
