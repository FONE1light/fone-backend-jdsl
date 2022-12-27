package com.fone.filmone.presentation.question

import com.fone.filmone.domain.question.entity.Question
import com.fone.filmone.domain.question.enum.Type

class QuestionRegisterDto {

    data class QuestionRegisterRequest(
        val email: String,
        val type: Type,
        val title: String,
        val description: String,
        val agreeToPersonalInformation: Boolean,
    ) {

        fun toEntity(): Question {

            return Question(
                email = email,
                type = type,
                title = title,
                description = description,
                agreeToPersonalInformation = agreeToPersonalInformation,
            )
        }
    }

    data class QuestionRegisterResponse(
        val email: String,
        val type: Type,
        val title: String,
        val description: String,
        val agreeToPersonalInformation: Boolean,
    ) {

        constructor(
            question: Question
        ) : this(
            email = question.email,
            type = question.type,
            title = question.title,
            description = question.description,
            agreeToPersonalInformation = question.agreeToPersonalInformation,
        )
    }
}