package com.fone.filmone.domain.common

enum class Type {
    ACTOR,
    STAFF,
    ;

    companion object {
        operator fun invoke(type: String) = Type.valueOf(type.uppercase())
    }
}