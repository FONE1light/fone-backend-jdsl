package com.fone.user.presentation.dto

import com.fone.user.domain.enum.LoginType
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern

class SMSUserDto {

    data class SMSRequest(
        @field:Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}\$")
        @ApiModelProperty(value = "휴대폰 번호", example = "010-1234-1234", required = true)
        val phoneNumber: String,
    )

    data class SMSValidationRequest(
        @field:Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}\$")
        @ApiModelProperty(value = "휴대폰 번호", example = "010-1234-1234", required = true)
        val phoneNumber: String,
        @field:NotEmpty(message = "인증번호는 필수 값 입니다.")
        @ApiModelProperty(value = "인증번호", example = "123456", required = true)
        val code: String,
    )

    data class PasswordSMSValidationResponse(
        val token: String? = null,
        val loginType: LoginType,
    )

    data class UserInfoSMSValidationResponse(
        val loginType: LoginType,
        val email: String?,
    )
}
