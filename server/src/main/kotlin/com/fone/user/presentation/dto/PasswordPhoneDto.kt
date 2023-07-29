package com.fone.user.presentation.dto

import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

class PasswordPhoneDto {

    data class PasswordPhoneSMSRequest(
        @field:NotEmpty(message = "이메일은 필수 값 입니다.")
        @field:Email(message = "유효하지 않는 이메일 입니다.")
        @ApiModelProperty(value = "이메일", example = "test@test.com", required = true)
        val email: String,
    )

    data class PasswordPhoneSMSResponse(
        val response: ResponseType,
    )

    data class PasswordPhoneValidationRequest(
        @field:NotEmpty(message = "이메일은 필수 값 입니다.")
        @field:Email(message = "유효하지 않는 이메일 입니다.")
        @ApiModelProperty(value = "이메일", example = "test@test.com", required = true)
        val email: String,
        @field:NotEmpty(message = "인증번호는 필수 값 입니다.")
        @ApiModelProperty(value = "인증번호", example = "123456", required = true)
        val code: String,
    )

    data class PasswordPhoneValidationResponse(
        val response: ResponseType,
        val token: String? = null,
    )

    enum class ResponseType {
        SUCCESS, FAILURE
    }
}
