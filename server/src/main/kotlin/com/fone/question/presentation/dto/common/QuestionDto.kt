package com.fone.question.presentation.dto.common

import com.fone.question.domain.entity.Question
import com.fone.question.domain.enum.Type

data class QuestionDto(
    val id: Long,
    val email: String,
    val type: Type,
    val title: String,
    val description: String,
    val agreeToPersonalInformation: Boolean,
) {

    constructor(
        question: Question,
    ) : this(
        id = question.id!!,
        email = question.email,
        type = question.type,
        title = question.title,
        description = question.description,
        agreeToPersonalInformation = question.agreeToPersonalInformation
    )
}
