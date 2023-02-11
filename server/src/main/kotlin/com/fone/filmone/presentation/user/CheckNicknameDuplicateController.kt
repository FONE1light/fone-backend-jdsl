package com.fone.filmone.presentation.user

import com.fone.common.response.CommonResponse
import com.fone.filmone.application.user.CheckNicknameDuplicationFacade
import com.fone.filmone.presentation.user.CheckNicknameDuplicateDto.CheckNicknameDuplicateRequest
import com.fone.filmone.presentation.user.CheckNicknameDuplicateDto.CheckNicknameDuplicateResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Api(tags = ["01. User Info"], description = "유저 서비스")
@RestController
@RequestMapping("/api/v1/user")
class CheckNicknameDuplicateController(
    private val checkNicknameDuplicateFacade: CheckNicknameDuplicationFacade,
) {

    @GetMapping("/check-nickname-duplication")
    @ApiOperation(value = "닉네임 중복체크 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = CheckNicknameDuplicateResponse::class))],
    )
    suspend fun checkNicknameDuplicate(@Valid @ModelAttribute request: CheckNicknameDuplicateRequest):
            CommonResponse<CheckNicknameDuplicateResponse> {
        val response = checkNicknameDuplicateFacade.check(request)
        return CommonResponse.success(response)
    }
}