package com.fone.filmone.domain.common

enum class Interest(interest: String) {
    FEATURE_FILM("장편영화"),
    SHORT_FILM("단편영화"),
    INDEPENDENT_FILM("독립영화"),
    WEB_DRAMA("웹 드라마"),
    MOVIE("뮤비 / CF"),
    OTT_DRAMA("OTT/TV 드라마"),
    YOUTUBE("유튜브"),
    VIRAL("홍보 / 바이럴"),
    ETC("기타");

    companion object {
        operator fun invoke(interest: String) = Interest.valueOf(interest.uppercase())
    }
}