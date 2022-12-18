package com.fone.filmone.domain.user

import java.util.*

class UserInfo {

    class Main(
        var loginId: String,
        var enabled: Boolean,
        var roles: List<RoleInfo>? = null
    ) {
        constructor() : this(
            "",
            true,
            null
        )
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