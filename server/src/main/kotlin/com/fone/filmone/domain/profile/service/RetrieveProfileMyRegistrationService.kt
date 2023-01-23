package com.fone.filmone.domain.profile.service

import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.domain.profile.repository.ProfileRepository
import com.fone.filmone.domain.user.repository.UserRepository
import com.fone.filmone.presentation.profile.RetrieveProfileMyRegistrationDto.RetrieveProfileMyRegistrationResponse
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
        val profiles = profileRepository.findByUserId(user.id!!) as ArrayList

        return RetrieveProfileMyRegistrationResponse(profiles)
    }
}