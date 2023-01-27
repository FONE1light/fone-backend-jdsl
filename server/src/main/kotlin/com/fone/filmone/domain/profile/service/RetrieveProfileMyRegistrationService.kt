package com.fone.filmone.domain.profile.service

import com.fone.common.exception.NotFoundUserException
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

            RetrieveProfileMyRegistrationResponse(
                profiles.await(),
                userProfileWants.await(),
                pageable
            )
        }
    }
}