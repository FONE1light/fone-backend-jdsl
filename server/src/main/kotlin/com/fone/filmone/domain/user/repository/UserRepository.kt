package com.fone.filmone.domain.user.repository

import com.fone.filmone.domain.user.entity.User
import com.fone.filmone.domain.user.enum.SocialLoginType

interface UserRepository {

    suspend fun findByEmailAndSocialLoginType(
        email: String,
        socialLoginType: SocialLoginType,
    ): User?

    suspend fun findByNicknameOrEmail(nickname: String?, email: String?): User?

    suspend fun save(newUser: User): User
}