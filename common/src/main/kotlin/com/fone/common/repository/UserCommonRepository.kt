package com.fone.common.repository

interface UserCommonRepository {

    suspend fun findByEmail(email: String): Long?

    suspend fun findJobByEmail(email: String): String?

    suspend fun findNicknameByEmail(email: String): String?
}
