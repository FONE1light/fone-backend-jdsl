package com.fone.filmone.domain.profile.repository

import com.fone.filmone.domain.profile.entity.ProfileImage

interface ProfileImageRepository {
    suspend fun saveAll(profileImages: List<ProfileImage>): List<ProfileImage>
}