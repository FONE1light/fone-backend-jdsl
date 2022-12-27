package com.fone.filmone.presentation.auth

import com.fone.filmone.application.auth.SignInFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.presentation.auth.SignInDto.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user/v1/auth")
class SignInController(
    private val signInFacade: SignInFacade,
) {

    @PostMapping("/sign-in")
    suspend fun signIn(@RequestBody request: SignInRequest): CommonResponse<SignInResponse> {
        val response = signInFacade.signIn(request)
        return CommonResponse.success(response)
    }
}