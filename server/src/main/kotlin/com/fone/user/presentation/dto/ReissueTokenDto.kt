package com.fone.user.presentation.dto

class ReissueTokenDto {

    data class ReissueTokenRequest(
        val accessToken: String,
        val refreshToken: String,
    )
}
