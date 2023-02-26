package com.fone.user.domain.repository

import com.fone.user.domain.entity.User
import com.fone.user.domain.enum.SocialLoginType

interface UserRepository {

    suspend fun findByEmailAndSocialLoginType(
        email: String,
        socialLoginType: SocialLoginType,
    ): User?

    suspend fun findByNicknameOrEmail(nickname: String?, email: String?): User?

    suspend fun save(newUser: User): User
}
