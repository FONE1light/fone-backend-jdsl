package com.fone.filmone.domain.profile.service

import com.fone.common.exception.NotFoundUserException
import com.fone.filmone.domain.profile.repository.ProfileCategoryRepository
import com.fone.filmone.domain.profile.repository.ProfileDomainRepository
import com.fone.filmone.domain.profile.repository.ProfileRepository
import com.fone.filmone.domain.profile.repository.ProfileWantRepository
import com.fone.filmone.domain.user.repository.UserRepository
import com.fone.filmone.presentation.profile.RetrieveProfileMyRegistrationDto.RetrieveProfileMyRegistrationResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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
    suspend fun retrieveProfileMyRegistration(pageable: Pageable, email: String):
            RetrieveProfileMyRegistrationResponse {
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()

        return coroutineScope {
            val profiles = async {
                profileRepository.findAllByUserId(pageable, user.id!!).content
            }

            val userProfileWants = async {
                profileWantRepository.findByUserId(user.id!!)
            }

            val profileIds = profiles.await().map { it.id!! }.toList()

            val profileDomains = async {
                profileDomainRepository.findByProfileIds(profileIds)
            }

            val profileCategories = async {
                profileCategoryRepository.findByProfileIds(profileIds)
            }

            RetrieveProfileMyRegistrationResponse(
                profiles.await(),
                userProfileWants.await(),
                profileDomains.await(),
                profileCategories.await(),
                pageable,
            )
        }
    }
}