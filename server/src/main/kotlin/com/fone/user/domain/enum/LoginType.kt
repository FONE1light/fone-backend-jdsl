package com.fone.user.domain.enum

enum class LoginType {
    KAKAO, NAVER, GOOGLE, APPLE, PASSWORD;

    companion object {
        operator fun invoke(loginType: String) = LoginType.valueOf(loginType.uppercase())
    }
}
