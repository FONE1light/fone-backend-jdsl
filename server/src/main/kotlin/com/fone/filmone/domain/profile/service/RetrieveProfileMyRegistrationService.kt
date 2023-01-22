package com.fone.filmone.domain.profile.service

import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.infrastructure.profile.ProfileRepository
import com.fone.filmone.infrastructure.user.UserRepository
import com.fone.filmone.presentation.profile.RetrieveProfileMyRegistrationDto.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveProfileMyRegistrationService(
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveProfileMyRegistration(email: String):
            RetrieveProfileMyRegistrationResponse {
        val user = userRepository.findByEmail(email) ?: throw NotFoundUserException()
        val profiles = profileRepository.findByUserId(user.id!!)

        return RetrieveProfileMyRegistrationResponse(profiles)
    }
}