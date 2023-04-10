package com.fone.user.domain.entity

import com.fone.user.domain.enum.LoginType

data class OauthPrincipal(
    val type: LoginType,
    val email: String,
    val identifier: String? = null,
)
