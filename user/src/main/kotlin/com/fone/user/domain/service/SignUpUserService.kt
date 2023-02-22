package com.fone.user.domain.service

import com.fone.common.exception.DuplicateUserException
import com.fone.user.domain.repository.UserRepository
import com.fone.user.presentation.dto.SignUpUserDto.SignUpUserRequest
import com.fone.user.presentation.dto.SignUpUserDto.SignUpUserResponse
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