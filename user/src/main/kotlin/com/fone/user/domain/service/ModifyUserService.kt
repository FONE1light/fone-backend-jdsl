package com.fone.user.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.user.domain.repository.UserRepository
import com.fone.user.presentation.dto.ModifyUserDto.ModifyUserRequest
import com.fone.user.presentation.dto.ModifyUserDto.ModifyUserResponse
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