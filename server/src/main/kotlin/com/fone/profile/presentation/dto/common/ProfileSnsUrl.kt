package com.fone.profile.presentation.dto.common

import com.fone.profile.domain.entity.ProfileSns
import com.fone.profile.domain.enum.SNS

data class ProfileSnsUrl(
    val url: String,
    val sns: SNS,
) {
    fun toEntity() = ProfileSns(url, sns)
}

fun ProfileSns.toDto() = ProfileSnsUrl(url, sns)
