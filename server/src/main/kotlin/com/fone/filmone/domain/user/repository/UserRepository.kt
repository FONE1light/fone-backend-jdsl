package com.fone.filmone.domain.user.repository

import com.fone.filmone.domain.user.entity.User

interface UserRepository {

    suspend fun findByEmailAndSocialLoginType(email: String, socialLoginType: String): User?
    suspend fun findByNickname(nickname: String): User?

    suspend fun findByNicknameOrEmail(nickname: String, email: String): User?

    suspend fun findByEmail(email: String): User?
}