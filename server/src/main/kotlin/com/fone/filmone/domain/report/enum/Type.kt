package com.fone.filmone.domain.report.enum

enum class Type {
    JOB_OPENING,
    PROFILE,
    CHATTING,
    ;

    companion object {
        operator fun invoke(type: String) = Type.valueOf(type.uppercase())
    }
}