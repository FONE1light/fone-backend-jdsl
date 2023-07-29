package com.fone.user.domain.repository

import com.fone.user.domain.enum.LoginType

interface UserRepository {

    suspend fun findByEmailAndLoginType(
        email: String,
        loginType: LoginType,
    ): com.fone.user.domain.entity.User?

    suspend fun findByIdentifier(identifier: String): com.fone.user.domain.entity.User?

    suspend fun findByPhone(phone: String): com.fone.user.domain.entity.User?

    suspend fun findByNicknameOrEmail(nickname: String?, email: String?): com.fone.user.domain.entity.User?

    suspend fun save(newUser: com.fone.user.domain.entity.User): com.fone.user.domain.entity.User
}
