package com.fone.filmone.domain.user.service

import com.fone.filmone.common.exception.DuplicateUserException
import com.fone.filmone.infrastructure.user.UserRepository
import com.fone.filmone.presentation.user.SignUpUserDto.SignUpUserRequest
import com.fone.filmone.presentation.user.SignUpUserDto.SignUpUserResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SignUpUserService(
    private val userRepository: UserRepository,
) {

    @Transactional
    suspend fun signUpUser(request: SignUpUserRequest): SignUpUserResponse {
        with(request) {
            userRepository.findByNicknameOrEmail(nickname, email)?.let {
                throw DuplicateUserException()
            }

            val newUser = toEntity()
            userRepository.save(newUser)
            return SignUpUserResponse(newUser)
        }
    }
}