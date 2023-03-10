package com.fone.user.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.user.application.SignInUserFacade
import com.fone.user.presentation.dto.SignInUserDto.SignInUserRequest
import com.fone.user.presentation.dto.SignInUserDto.SignInUserResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
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

    @PostMapping("/sign-in")
    @ApiOperation(value = "로그인 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공"
    )
    suspend fun signInUser(
        @Valid @RequestBody
        request: SignInUserRequest,
    ): CommonResponse<SignInUserResponse> {
        val response = signInUserFacade.signIn(request)
        return CommonResponse.success(response)
    }
}
