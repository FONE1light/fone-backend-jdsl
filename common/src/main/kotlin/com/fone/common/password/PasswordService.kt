package com.fone.common.password

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

object PasswordService {

    private val encoder = BCryptPasswordEncoder()
    fun hashPassword(password: String): String {
        return encoder.encode(password)
    }

    fun isValidPassword(challenge: String, answer: String): Boolean {
        return encoder.matches(challenge, answer)
    }
}
