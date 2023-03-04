package com.fone.common.entity

enum class Type {
    ACTOR,
    STAFF,

    ;

    companion object {
        operator fun invoke(type: String) = Type.valueOf(type.uppercase())
    }
}
