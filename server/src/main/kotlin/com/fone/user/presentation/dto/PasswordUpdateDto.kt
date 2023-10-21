package com.fone.user.presentation.dto

import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern

class PasswordUpdateDto {

    data class PasswordUpdateRequest(
        @field:Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}\$")
        @ApiModelProperty(value = "휴대폰 번호", example = "010-1234-1234", required = true)
        val phoneNumber: String,
        @field:NotEmpty(message = "비밀번호는 필수 값 입니다.")
        @field:Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[\\W_])[A-Za-z\\d\\W_]{8,16}\$",
            message = "영소문자, 영대문자, 숫자, 특수문자가 포함된 8~16자 비밀번호"
        )
        val password: String,
        @field:NotEmpty(message = "변경 토큰은 필수 값 입니다.")
        val token: String,
    )
}
