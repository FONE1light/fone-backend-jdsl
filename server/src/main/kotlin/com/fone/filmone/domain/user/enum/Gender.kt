package com.fone.filmone.domain.user.enum

enum class Gender {
    MAN, WOMAN;

    companion object {

        operator fun invoke(priority: String) = valueOf(priority.uppercase())
    }
}