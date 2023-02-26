package com.fone.common.jwt

import java.util.*

data class Token(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: Long,
    val issuedAt: Date
)
