package com.fone.user.presentation.dto

import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class EmailSendRequest(
    @field:NotEmpty(message = "이메일은 필수 값 입니다.")
    @field:Email(message = "유효하지 않는 이메일 입니다.")
    @ApiModelProperty(value = "이메일", example = "test@test.com", required = true)
    val email: String,
)

data class EmailValidationRequest(
    @field:NotEmpty(message = "이메일은 필수 값 입니다.")
    @field:Email(message = "유효하지 않는 이메일 입니다.")
    @ApiModelProperty(value = "이메일", example = "test@test.com", required = true)
    val email: String,
    @ApiModelProperty(value = "인증번호", required = true)
    val code: String,
)

data class EmailValidationResponse(
    val token: String,
)

data class EmailDuplicationRequest(
    @field:NotEmpty(message = "이메일은 필수 값 입니다.")
    @field:Email(message = "유효하지 않는 이메일 입니다.")
    @ApiModelProperty(value = "이메일", example = "test@test.com", required = true)
    val email: String,
)

data class EmailDuplicationResponse(
    val email: String,
)
