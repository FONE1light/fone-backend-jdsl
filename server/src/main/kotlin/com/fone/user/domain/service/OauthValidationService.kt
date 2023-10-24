package com.fone.user.domain.service

import com.fone.user.domain.entity.OauthPrincipal
import com.fone.user.domain.enum.LoginType
import com.fone.user.domain.repository.OauthRepository
import org.springframework.stereotype.Service

@Service
class OauthValidationService(
    private val oauthRepositories: List<OauthRepository>,
) {
    suspend fun getAccessToken(authenticationCode: String, state: String?, type: LoginType): String {
        return oauthRepositories.findType(type).fetchAccessToken(authenticationCode, state)
    }

    suspend fun getPrincipal(loginType: LoginType, accessToken: String): OauthPrincipal {
        return oauthRepositories.findType(loginType).fetchPrincipal(accessToken)
    }

    // Sign-up의 경우 토큰의 유효성만 검증
    suspend fun isValidTokenSignUp(
        loginType: LoginType,
        accessToken: String,
        email: String,
        identifier: String?,
    ): Boolean {
        return when (loginType) {
            LoginType.APPLE -> {
                identifier == oauthRepositories.findType(loginType).fetchPrincipal(accessToken).identifier
            }

            else -> email == getPrincipal(loginType, accessToken).email
        }
    }

    private fun List<OauthRepository>.findType(type: LoginType) =
        this.first { repo -> repo.type == type }
}
