package com.fone.user.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.user.application.PasswordUpdateFacade
import com.fone.user.presentation.dto.PasswordUpdateDto.PasswordUpdateRequest
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Api(tags = ["01. User Info"], description = "유저 서비스")
@RestController
@RequestMapping("/api/v1/users")
class UpdatePasswordController(private val passwordUpdateFacade: PasswordUpdateFacade) {
    @PatchMapping("/password")
    @ApiOperation(value = "비밀번호 변경 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공"
    )
    suspend fun passwordUpdate(
        @Valid @RequestBody
        request: PasswordUpdateRequest,
    ): CommonResponse<Unit> {
        passwordUpdateFacade.update(request)
        return CommonResponse.success(Unit)
    }
}
