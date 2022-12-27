package com.fone.filmone.infrastructure.user

import com.fone.filmone.domain.user.entity.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepository : CoroutineCrudRepository<User, Long> {

    suspend fun findByEmailAndSocialLoginType(email: String, socialLoginType: String): User?
    suspend fun findByNickname(nickname: String): User?
}