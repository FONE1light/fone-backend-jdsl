package com.fone.user.domain.service

import com.fone.common.exception.ForbiddenException
import com.fone.common.exception.NotFoundUserException
import com.fone.user.domain.enum.Job
import com.fone.user.domain.repository.UserRepository
import com.fone.user.presentation.dto.ModifyUserDto.AdminModifyUserRequest
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
        if (request.job == Job.VERIFIED) {
            throw ForbiddenException()
        }
        with(request) {
            val user = userRepository.findByNicknameOrEmail(null, email) ?: throw NotFoundUserException()

            user.modifyUser(this)
            userRepository.save(user)
            return ModifyUserResponse(user)
        }
    }

    @Transactional
    suspend fun adminModifyUser(request: AdminModifyUserRequest, id: Long): ModifyUserResponse {
        with(request) {
            val user = userRepository.findById(id) ?: throw NotFoundUserException()

            user.adminModifyUser(this)
            userRepository.save(user)
            return ModifyUserResponse(user)
        }
    }
}
