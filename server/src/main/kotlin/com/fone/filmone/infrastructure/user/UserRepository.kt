package com.fone.filmone.infrastructure.user

import com.fone.filmone.domain.user.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepository : CoroutineCrudRepository<User, Long> {
}