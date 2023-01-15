package com.fone.filmone.presentation.user

import com.fone.filmone.application.user.CheckNicknameDuplicationFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.presentation.user.CheckNicknameDuplicateDto.CheckNicknameDuplicateRequest
import com.fone.filmone.presentation.user.CheckNicknameDuplicateDto.CheckNicknameDuplicateResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/user")
class CheckNicknameDuplicateController(
    private val checkNicknameDuplicateFacade: CheckNicknameDuplicationFacade
) {

    @GetMapping("/check-nickname-duplication")
    suspend fun checkNicknameDuplicate(@Valid @ModelAttribute request: CheckNicknameDuplicateRequest):
            CommonResponse<CheckNicknameDuplicateResponse> {
        val response = checkNicknameDuplicateFacade.check(request)
        return CommonResponse.success(response)
    }
}