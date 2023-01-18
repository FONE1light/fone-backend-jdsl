package com.fone.filmone.domain.profile.service

import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.domain.profile.entity.ProfileImage
import com.fone.filmone.infrastructure.profile.ProfileImageRepository
import com.fone.filmone.infrastructure.profile.ProfileRepository
import com.fone.filmone.infrastructure.user.UserRepository
import com.fone.filmone.presentation.profile.RegisterProfileDto.RegisterProfileRequest
import com.fone.filmone.presentation.profile.RegisterProfileDto.RegisterProfileResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.kotlin.core.publisher.toMono

@Service
class RegisterProfileService(
    private val profileRepository: ProfileRepository,
    private val profileImageRepository: ProfileImageRepository,
    private val userRepository: UserRepository,
) {

    @Transactional
    suspend fun registerProfile(
        request: RegisterProfileRequest,
        email: String,
    ): RegisterProfileResponse {
        val user = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        with(request) {
            val profile = toEntity(user.id!!)
            profileRepository.save(profile)

            val urls = this.profileUrls.map {
                ProfileImage(
                    profileId = profile.id!!,
                    profileUrl = it
                )
            }.toList()

            profileImageRepository.saveAll(urls).collect{ it.toMono().awaitSingle() }

            return RegisterProfileResponse(profile)
        }
    }
}