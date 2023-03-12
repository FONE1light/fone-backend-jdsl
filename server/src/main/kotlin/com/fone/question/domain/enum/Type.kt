package com.fone.question.domain.enum

import com.fone.report.domain.enum.Type

enum class Type {
    USE_QUESTION,
    VOICE_OF_THE_CUSTOMER,
    ALLIANCE,

    ;

    companion object {
        operator fun invoke(type: String) = Type.valueOf(type.uppercase())
    }
}
