package com.fone.common.jwt

interface JwtUserRepository {

    suspend fun validTokenByEmail(email: String): Boolean?
}
