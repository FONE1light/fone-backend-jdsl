package com.fone.user.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.user.domain.repository.UserRepository
import com.fone.user.presentation.dto.RetrieveMyPageUserResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveUserService(
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveUser(email: String): RetrieveMyPageUserResponse {
        val user = userRepository.findByNicknameOrEmail(null, email) ?: throw NotFoundUserException()

        return RetrieveMyPageUserResponse(user)
    }
}
