package com.fone.user.domain.repository

import com.fone.user.domain.entity.OauthPrincipal
import com.fone.user.domain.enum.LoginType

interface OauthRepository {
    val type: LoginType
    suspend fun fetchAccessToken(code: String, state: String?): String
    suspend fun fetchPrincipal(accessToken: String): OauthPrincipal
}
