package com.fone.filmone.infrastructure.profile

import com.fone.filmone.domain.profile.entity.ProfileImage
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ProfileImageRepository : CoroutineCrudRepository<ProfileImage, Long>