@file:Suppress("ktlint")

package com.fone.user.presentation.dto

data class ReissueTokenRequest(
    val accessToken: String,
    val refreshToken: String,
)
