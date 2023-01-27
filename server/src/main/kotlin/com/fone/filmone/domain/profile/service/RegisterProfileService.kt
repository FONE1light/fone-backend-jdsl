package com.fone.filmone.domain.profile.service

import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.domain.profile.entity.ProfileImage
import com.fone.filmone.domain.profile.repository.ProfileImageRepository
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
    private val profileImageRepository: ProfileImageRepository,
    private val profileWantRepository: ProfileWantRepository,
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
                    profileRepository.save(profile)

                    val urls = profileUrls.map {
                        ProfileImage(
                            profileId = profile.id!!,
                            profileUrl = it
                        )
                    }.toList()
                    profileImageRepository.saveAll(urls)

                    profile
                }

                val userProfileWants = async {
                    profileWantRepository.findByUserId(user.id!!)
                }

                RegisterProfileResponse(profile.await(), userProfileWants.await())
            }
        }
    }
}