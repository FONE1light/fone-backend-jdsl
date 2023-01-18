package com.fone.filmone.infrastructure.profile

import com.fone.filmone.domain.profile.entity.Profile
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ProfileRepository : CoroutineCrudRepository<Profile, Long>