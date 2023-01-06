package com.fone.filmone.presentation.auth

import javax.validation.constraints.NotEmpty

class CheckNicknameDuplicateDto {

    data class CheckNicknameDuplicateRequest(
        @field:NotEmpty(message = "닉네임은 필수 값 입니다.")
        val nickname: String,
    )

    data class CheckNicknameDuplicateResponse(
        val nickname: String,
        val isDuplicate: Boolean,
    )
}