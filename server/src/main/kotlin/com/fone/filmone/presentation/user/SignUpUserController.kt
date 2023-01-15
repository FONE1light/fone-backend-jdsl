package com.fone.filmone.presentation.user

import com.fone.filmone.application.user.SignUpUserFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.presentation.user.SignUpUserDto.SignUpUserRequest
import com.fone.filmone.presentation.user.SignUpUserDto.SignUpUserResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/user")
class SignUpUserController(
    private val signUpUserFacade: SignUpUserFacade,
) {

    @PostMapping("/sign-up")
    suspend fun signUp(@Valid @RequestBody request: SignUpUserRequest): CommonResponse<SignUpUserResponse> {
        val response = signUpUserFacade.signUp(request)
        return CommonResponse.success(response)
    }
}