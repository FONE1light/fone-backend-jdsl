package com.fone.filmone.domain.user.service

import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.infrastructure.user.UserRepository
import org.springframework.stereotype.Service

@Service
class SignOutUserService(
    private val userRepository: UserRepository,
) {

    suspend fun signOutUser(email: String) {
        val user = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        user.signOutUser()

        userRepository.save(user)
    }
}