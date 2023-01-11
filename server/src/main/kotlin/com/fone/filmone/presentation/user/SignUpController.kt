package com.fone.filmone.presentation.user

import com.fone.filmone.application.user.SignUpFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.presentation.user.SignUpDto.SignUpRequest
import com.fone.filmone.presentation.user.SignUpDto.SignUpResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/user")
class SignUpController(
    private val signUpFacade: SignUpFacade,
) {

    @PostMapping("/sign-up")
    suspend fun signUp(@Valid @RequestBody request: SignUpRequest): CommonResponse<SignUpResponse> {
        val response = signUpFacade.signUp(request)
        return CommonResponse.success(response)
    }
}