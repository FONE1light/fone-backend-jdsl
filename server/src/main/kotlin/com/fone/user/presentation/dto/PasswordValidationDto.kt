package com.fone.user.presentation.dto

import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern

data class PasswordValidationRequest(
    @field:NotEmpty(message = "이메일은 필수 값 입니다.")
    @field:Email(message = "유효하지 않는 이메일 입니다.")
    @ApiModelProperty(value = "이메일", example = "test@test.com", required = true)
    val email: String,
    @field:NotEmpty(message = "비밀번호는 필수 값 입니다.")
    @field:Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
        message = "영문자, 숫자, 특수문자가 포함된 8~16자 비밀번호"
    )
    val password: String,
)

data class PasswordValidationResponse(
    val response: ResponseType,
)

enum class ResponseType {
    VALID, INVALID
}
