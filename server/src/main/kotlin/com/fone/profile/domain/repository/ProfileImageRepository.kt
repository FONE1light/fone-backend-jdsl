package com.fone.profile.domain.repository

import com.fone.profile.domain.entity.ProfileImage

interface ProfileImageRepository {
    suspend fun saveAll(images: List<ProfileImage>): List<ProfileImage>

    suspend fun findAll(profileId: Long): List<ProfileImage>

    suspend fun deleteByProfileId(profileId: Long): Int
}
