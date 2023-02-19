package com.fone.user.infrastructure

import com.fone.common.repository.UserCommonRepository
import com.fone.user.domain.repository.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserCommonRepositoryImpl(
    private val userRepository: UserRepository,
) : UserCommonRepository {

    override suspend fun findByEmail(email: String): Long? {
        val user = userRepository.findByNicknameOrEmail(null, email) ?: return null

        return user.id
    }

    override suspend fun findJobByEmail(email: String): String? {
        val user = userRepository.findByNicknameOrEmail(null, email) ?: return null

        return user.job.toString()
    }
}