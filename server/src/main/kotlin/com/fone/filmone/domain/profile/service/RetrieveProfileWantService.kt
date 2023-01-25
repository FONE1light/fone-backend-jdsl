package com.fone.filmone.domain.profile.service

import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.profile.entity.ProfileWant
import com.fone.filmone.domain.profile.repository.ProfileRepository
import com.fone.filmone.domain.profile.repository.ProfileWantRepository
import com.fone.filmone.domain.user.repository.UserRepository
import com.fone.filmone.presentation.profile.RetrieveProfileWantDto.RetrieveProfileWantResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveProfileWantService(
    private val profileWantRepository: ProfileWantRepository,
    private val profileRepository: ProfileRepository,
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveProfileWant(
        email: String,
        type: Type,
    ): RetrieveProfileWantResponse {
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()
        val profileWants = profileWantRepository.findByUserId(user.id!!)
        val profileIds = profileWants
            .map(ProfileWant::profileId)
            .toList()

        val profiles = profileRepository.findAllById(profileIds)
            .filter {
                it.type == type
            }.toList() as ArrayList

        return RetrieveProfileWantResponse(profiles)
    }
}