package com.fone.filmone.presentation.question

import com.fone.filmone.domain.question.entity.Question
import com.fone.filmone.domain.question.enum.Type
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class QuestionRegisterDto {

    data class QuestionRegisterRequest(
        @field:NotEmpty(message = "이메일은 필수 값 입니다.")
        val email: String,
        @field:NotNull(message = "상담유형은 필수 값 입니다.")
        val type: Type,
        @field:NotEmpty(message = "제목은 필수 값 입니다.")
        val title: String,
        @field:NotEmpty(message = "설명은 필수 값 입니다.")
        val description: String,
        @field:AssertTrue(message = "개인정보 수집 및 이용동의 선택은 필수 값 입니다.")
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