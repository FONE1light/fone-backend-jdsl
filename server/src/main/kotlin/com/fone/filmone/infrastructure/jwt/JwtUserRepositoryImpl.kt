package com.fone.filmone.infrastructure.jwt

import com.fone.common.jwt.JwtUserRepository
import com.fone.filmone.domain.user.repository.UserRepository
import org.springframework.stereotype.Repository

@Repository
class JwtUserRepositoryImpl(
    private val userJpaRepository: UserRepository,
) : JwtUserRepository {

    override suspend fun validTokenByEmail(email: String): Boolean? {
        val user = userJpaRepository.findByNicknameOrEmail(null, email)
            ?: return null
        if (!user.enabled) {
            return null
        }

        return true
    }
}