package com.fone.user.presentation.dto

import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotEmpty

class CheckNicknameDuplicateDto {

    data class CheckNicknameDuplicateRequest(
        @field:NotEmpty(message = "닉네임은 필수 값 입니다.")
        @ApiModelProperty(
            value = "닉네임",
            example = "테스트닉네임",
            required = true
        )
        val nickname: String,
    )

    data class CheckNicknameDuplicateResponse(
        val nickname: String,
        val isDuplicate: Boolean,
    )
}
