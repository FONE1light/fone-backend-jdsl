package com.fone.profile.presentation.dto.common

import com.fone.profile.domain.entity.ProfileSns
import com.fone.profile.domain.enum.SNS
import io.swagger.v3.oas.annotations.media.Schema

data class ProfileSnsUrl(
    val url: String,
    @field:Schema(description = "Sns URL", example = "https://www.youtube.com/channel")
    val sns: SNS,
) {
    fun toEntity() = ProfileSns(url, sns)
}

fun ProfileSns.toDto() = ProfileSnsUrl(url, sns)
