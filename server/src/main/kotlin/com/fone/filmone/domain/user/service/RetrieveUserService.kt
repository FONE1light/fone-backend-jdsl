package com.fone.filmone.domain.user.service

import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.infrastructure.user.UserRepository
import com.fone.filmone.presentation.user.RetrieveMyPageUserDto.RetrieveMyPageUserResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveUserService(
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveUser(email: String): RetrieveMyPageUserResponse {

        val user = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        return RetrieveMyPageUserResponse(user)
    }
}