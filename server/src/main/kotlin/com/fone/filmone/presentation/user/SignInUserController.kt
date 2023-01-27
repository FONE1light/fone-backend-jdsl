package com.fone.filmone.presentation.user

import com.fone.filmone.application.user.SignInUserFacade
import com.fone.common.response.CommonResponse
import com.fone.filmone.presentation.user.SignInUserDto.SignInUserRequest
import com.fone.filmone.presentation.user.SignInUserDto.SignInUserResponse
import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Api(tags = ["01. User Info"], description = "유저 서비스")
@RestController
@RequestMapping("/api/v1/user")
class SignInUserController(
    private val signInUserFacade: SignInUserFacade,
) {

    @PostMapping("/sign-in")
    suspend fun signInUser(@Valid @RequestBody request: SignInUserRequest): CommonResponse<SignInUserResponse> {
        val response = signInUserFacade.signIn(request)
        return CommonResponse.success(response)
    }
}