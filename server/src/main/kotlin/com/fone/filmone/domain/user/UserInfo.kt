package com.fone.filmone.domain.user

import java.util.*

class UserInfo {

    data class Main(
        val job: String,
        val interests: String,
        val nickname: String,
        val birthday: String,
        val gender: String,
        val profileUrl: String,
        val phoneNumber: String,
        val email: String,
        val providerType: String,
        var roles: List<RoleInfo>? = null
    ) {

    }

    class RoleInfo(
        var role: String
    )

    class Token(
        var accessToken: String,
        var tokenType: String,
        var expiresIn: Long,
        var issuedAt: Date
    )
}