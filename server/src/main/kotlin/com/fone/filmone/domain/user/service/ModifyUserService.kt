package com.fone.filmone.domain.user.service

import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.domain.user.repository.UserRepository
import com.fone.filmone.presentation.user.ModifyUserDto.ModifyUserRequest
import com.fone.filmone.presentation.user.ModifyUserDto.ModifyUserResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ModifyUserService(
    private val userRepository: UserRepository,
) {

    @Transactional
    suspend fun modifyUser(request: ModifyUserRequest, email: String): ModifyUserResponse {
        with(request) {
            val user = userRepository.findByNicknameOrEmail(null, email)
                ?: throw NotFoundUserException()

            user.modifyUser(this)
            userRepository.save(user)
            return ModifyUserResponse(user)
        }
    }
}