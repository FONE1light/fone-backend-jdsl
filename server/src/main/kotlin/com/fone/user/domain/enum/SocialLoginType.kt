package com.fone.user.domain.enum

enum class SocialLoginType {
    KAKAO, NAVER, GOOGLE, APPLE,
    ;

    companion object {
        operator fun invoke(socialLoginType: String) = SocialLoginType.valueOf(socialLoginType.uppercase())
    }
}
