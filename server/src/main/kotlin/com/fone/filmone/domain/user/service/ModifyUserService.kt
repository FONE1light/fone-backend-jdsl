package com.fone.filmone.domain.user.service

import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.infrastructure.user.UserRepository
import com.fone.filmone.presentation.user.ModifyUserDto.ModifyUserRequest
import com.fone.filmone.presentation.user.ModifyUserDto.ModifyUserResponse
import org.springframework.stereotype.Service

@Service
class ModifyUserService(
    private val userRepository: UserRepository
) {

    suspend fun modifyUser(request: ModifyUserRequest): ModifyUserResponse {
        with(request) {
            val user = userRepository.findByEmail(email) ?: throw NotFoundUserException()

            user.modifyUser(this)
            userRepository.save(user)
            return ModifyUserResponse(user)
        }
    }
}