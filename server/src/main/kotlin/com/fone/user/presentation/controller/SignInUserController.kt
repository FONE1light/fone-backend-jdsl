package com.fone.user.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.user.application.SignInUserFacade
import com.fone.user.presentation.dto.EmailSignInUserRequest
import com.fone.user.presentation.dto.SignInUserResponse
import com.fone.user.presentation.dto.SocialSignInUserRequest
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
class SignInUserController(
    private val signInUserFacade: SignInUserFacade,
) {

    @PostMapping("/social/sign-in")
    @ApiOperation(value = "로그인 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = SignInUserResponse::class))]
    )
    suspend fun socialSignInUser(
        @Valid @RequestBody
        request: SocialSignInUserRequest,
    ): CommonResponse<SignInUserResponse> {
        val response = signInUserFacade.signIn(request)
        return CommonResponse.success(response)
    }

    @PostMapping("/email/sign-in")
    @ApiOperation(value = "로그인 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = SignInUserResponse::class))]
    )
    suspend fun emailSignInUser(
        @Valid @RequestBody
        request: EmailSignInUserRequest,
    ): CommonResponse<SignInUserResponse> {
        val response = signInUserFacade.signIn(request)
        return CommonResponse.success(response)
    }
}
