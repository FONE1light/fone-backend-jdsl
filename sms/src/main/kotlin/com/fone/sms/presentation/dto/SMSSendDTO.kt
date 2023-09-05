package com.fone.sms.presentation.dto

import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Pattern

class SMSSendDTO {
    data class SMSSendRequest(
        @field:Pattern(regexp = "^01\\d{8,9}\$")
        @ApiModelProperty(value = "휴대폰 번호", example = "01012341234", required = true)
        val phone: String,
        val code: String,
    )
    data class SMSSendResponse(val messageId: String)
}
