package com.fone.filmone.domain.common

enum class Gender {
    MAN, WOMAN, IRRELEVANT;

    companion object {

        operator fun invoke(priority: String) = valueOf(priority.uppercase())
    }
}