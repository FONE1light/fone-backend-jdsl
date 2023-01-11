package com.fone.filmone.presentation.user

import com.fone.filmone.application.user.SignInFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.presentation.user.SignInDto.SignInRequest
import com.fone.filmone.presentation.user.SignInDto.SignInResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/user")
class SignInController(
    private val signInFacade: SignInFacade,
) {

    @PostMapping("/sign-in")
    suspend fun signIn(@Valid @RequestBody request: SignInRequest): CommonResponse<SignInResponse> {
        val response = signInFacade.signIn(request)
        return CommonResponse.success(response)
    }
}