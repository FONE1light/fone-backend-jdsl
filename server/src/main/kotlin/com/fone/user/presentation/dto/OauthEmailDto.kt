package com.fone.user.presentation.dto

import com.fone.user.domain.enum.LoginType
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class OauthEmailDto {

    data class OauthEmailRequest(
        @field:NotNull(message = "소셜로그인 타입은 필수 값 입니다.") val loginType: LoginType,
        @field:NotEmpty(message = "accessToken은 필수 값 입니다.") val accessToken: String,
    ) {
        fun toResponse(email: String, isMember: Boolean) = OauthEmailResponse(
            loginType,
            email,
            isMember
        )
    }

    data class OauthEmailResponse(
        val loginType: LoginType,
        val email: String,
        val membership: Boolean,
    )
}
