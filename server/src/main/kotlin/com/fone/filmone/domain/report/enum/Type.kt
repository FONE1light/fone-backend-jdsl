package com.fone.filmone.domain.report.enum

import com.fone.filmone.domain.user.enum.Job

enum class Type {
    JOB_OPENING,
    PROFILE,
    CHATTING,
    ;

    companion object {
        operator fun invoke(type: String) = Type.valueOf(type.uppercase())
    }
}