package com.fone.user.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.user.application.SignUpUserFacade
import com.fone.user.presentation.dto.SignUpUserDto.PasswordSignUpUserRequest
import com.fone.user.presentation.dto.SignUpUserDto.SignUpUserResponse
import com.fone.user.presentation.dto.SignUpUserDto.SocialSignUpUserRequest
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
class SignUpUserController(
    private val signUpUserFacade: SignUpUserFacade,
) {

    @PostMapping("/social/sign-up")
    @ApiOperation(value = "회원가입 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = SignUpUserResponse::class))]
    )
    suspend fun socialSignUp(
        @Valid @RequestBody
        request: SocialSignUpUserRequest,
    ): CommonResponse<SignUpUserResponse> {
        val response = signUpUserFacade.signUp(request)
        return CommonResponse.success(response)
    }

    @PostMapping("/password/sign-up")
    @ApiOperation(value = "회원가입 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = SignUpUserResponse::class))]
    )
    suspend fun passwordSignUp(
        @Valid @RequestBody
        request: PasswordSignUpUserRequest,
    ): CommonResponse<SignUpUserResponse> {
        val response = signUpUserFacade.signUp(request)
        return CommonResponse.success(response)
    }
}
