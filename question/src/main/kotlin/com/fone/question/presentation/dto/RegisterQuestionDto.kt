package com.fone.question.presentation.dto

import com.fone.question.domain.entity.Question
import com.fone.question.domain.enum.Type
import com.fone.question.presentation.dto.common.QuestionDto
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class RegisterQuestionDto {

    data class RegisterQuestionRequest(
        @field:NotEmpty(message = "이메일은 필수 값 입니다.")
        @field:Email(message = "유효하지 않는 이메일 입니다.")
        @ApiModelProperty(value = "이메일", example = "test@test.com", required = true)
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

    data class RegisterQuestionResponse(
        val question: QuestionDto
    ) {

        constructor(
            question: Question,
        ) : this(
            question = QuestionDto(question)
        )
    }
}