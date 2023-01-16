package com.fone.filmone.domain.common

enum class Career(name: String) {
    NEWCOMER("신입"),
    LESS_THAN_1YEARS("1년 미만"),
    LESS_THAN_3YEARS("1~3년"),
    LESS_THAN_6YEARS("4~6년"),
    LESS_THAN_10YEARS("7~10년"),
    MORE_THAN_10YEARS("10년~"),
    IRRELEVANT("경력 무관")
    ;

    companion object {
        operator fun invoke(name: String) = Interest.valueOf(name.uppercase())
    }
}