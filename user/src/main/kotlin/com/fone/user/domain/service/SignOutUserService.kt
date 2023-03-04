package com.fone.user.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.user.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SignOutUserService(
    private val userRepository: UserRepository,
) {

    @Transactional
    suspend fun signOutUser(email: String) {
        val user = userRepository.findByNicknameOrEmail(null, email) ?: throw NotFoundUserException()

        user.signOutUser()

        userRepository.save(user)
    }
}
