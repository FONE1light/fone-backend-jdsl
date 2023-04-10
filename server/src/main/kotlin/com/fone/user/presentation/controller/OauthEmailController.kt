package com.fone.user.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.user.application.OauthValidationFacade
import com.fone.user.presentation.dto.OauthEmailDto
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Api(tags = ["01. User Info"], description = "유저 서비스")
@RestController
@RequestMapping("/api/v1/users")
class OauthEmailController(
    private val oauthValidationFacade: OauthValidationFacade,
) {

    @PostMapping("/email")
    @ApiOperation(value = "소셜 로그인 이메일 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = OauthEmailDto.OauthEmailResponse::class))]
    )
    suspend fun fetchSocialEmail(
        @Valid @RequestBody
        request: OauthEmailDto.OauthEmailRequest,
    ): CommonResponse<OauthEmailDto.OauthEmailResponse> {
        val response = oauthValidationFacade.getEmail(request)
        return CommonResponse.success(response)
    }
}
