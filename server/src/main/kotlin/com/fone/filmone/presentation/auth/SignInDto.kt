package com.fone.filmone.presentation.auth

import com.fone.filmone.domain.user.Token
import com.fone.filmone.domain.user.entity.User
import com.fone.filmone.domain.user.enum.SocialLoginType

class SignInDto {

    data class SignInRequest(
        val socialLoginType: SocialLoginType,
        val email: String,
    )

    data class SignInResponse(
        val socialLoginType: SocialLoginType,
        val email: String,
        val token: Token,
    ) {

        constructor(
            user: User,
            token: Token,
        ) : this(
            socialLoginType = user.socialLoginType,
            email = user.email,
            token = token,
        )
    }
}