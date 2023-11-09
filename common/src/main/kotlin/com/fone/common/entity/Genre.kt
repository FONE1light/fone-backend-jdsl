package com.fone.common.entity

enum class Genre(name: String) {
    ACTION("액션"),
    DRAMA("드라마"),
    THRILLER("스릴러"),
    MUSICAL("뮤지컬"),
    ROMANCE("로맨스"),
    FANTASY("판타지"),
    DOCUMENTARY("다큐멘터리"),
    ETC("기타"),
    ;

    companion object {
        operator fun invoke(name: String) = Genre.valueOf(name.uppercase())
    }
}
