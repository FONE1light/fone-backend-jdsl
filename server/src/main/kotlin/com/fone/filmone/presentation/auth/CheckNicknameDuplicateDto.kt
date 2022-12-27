package com.fone.filmone.presentation.auth

class CheckNicknameDuplicateDto {

    data class CheckNicknameDuplicateRequest(
        val nickname: String,
    )

    data class CheckNicknameDuplicateResponse(
        val nickname: String,
        val isDuplicate: Boolean,
    )
}