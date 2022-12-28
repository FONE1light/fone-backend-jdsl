package com.fone.filmone.presentation.auth

import com.fone.filmone.application.auth.CheckNicknameDuplicationFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.presentation.auth.CheckNicknameDuplicateDto.CheckNicknameDuplicateRequest
import com.fone.filmone.presentation.auth.CheckNicknameDuplicateDto.CheckNicknameDuplicateResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user/v1/auth")
class CheckNicknameDuplicateController(
    private val checkNicknameDuplicateFacade: CheckNicknameDuplicationFacade
) {

    @GetMapping("/check-nickname-duplication")
    suspend fun checkNickname(@ModelAttribute request: CheckNicknameDuplicateRequest):
            CommonResponse<CheckNicknameDuplicateResponse> {
        val response = checkNicknameDuplicateFacade.check(request)
        return CommonResponse.success(response)
    }
}