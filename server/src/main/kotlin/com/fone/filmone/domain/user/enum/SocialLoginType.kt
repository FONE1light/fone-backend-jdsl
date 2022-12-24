package com.fone.filmone.domain.user.enum

enum class SocialLoginType {
    KAKAO,
    NAVER,
    GOOGLE,
    APPLE;

    companion object {
        operator fun invoke(type: String) = SocialLoginType.valueOf(type.uppercase())
    }
}