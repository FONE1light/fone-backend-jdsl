package com.fone.filmone.domain.user.service

import com.fone.filmone.common.exception.DuplicateUserException
import com.fone.filmone.infrastructure.user.UserRepository
import com.fone.filmone.presentation.auth.SignUpDto.SignUpRequest
import com.fone.filmone.presentation.auth.SignUpDto.SignUpResponse
import org.springframework.stereotype.Service

@Service
class RegisterUserService(
    private val userRepository: UserRepository,
) {

    suspend fun registerUser(request: SignUpRequest): SignUpResponse {
        with(request) {
            userRepository.findByNicknameOrEmail(nickname, email)?.let {
                throw DuplicateUserException()
            }

            val newUser = toEntity()
            userRepository.save(newUser)
            return SignUpResponse(newUser)
        }
    }
}