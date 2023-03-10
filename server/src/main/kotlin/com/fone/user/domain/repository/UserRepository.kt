package com.fone.user.domain.repository

import com.fone.user.domain.enum.SocialLoginType

interface UserRepository {

    suspend fun findByEmailAndSocialLoginType(
        email: String,
        socialLoginType: SocialLoginType,
    ): com.fone.user.domain.entity.User?

    suspend fun findByNicknameOrEmail(nickname: String?, email: String?): com.fone.user.domain.entity.User?

    suspend fun save(newUser: com.fone.user.domain.entity.User): com.fone.user.domain.entity.User
}
