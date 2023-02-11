package com.fone.filmone.presentation.user

import com.fone.common.response.CommonResponse
import com.fone.filmone.application.user.ModifyUserFacade
import com.fone.filmone.presentation.user.ModifyUserDto.ModifyUserRequest
import com.fone.filmone.presentation.user.ModifyUserDto.ModifyUserResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.validation.Valid

@Api(tags = ["01. User Info"], description = "유저 서비스")
@RestController
@RequestMapping("/api/v1/user")
class ModifyUserController(
    private val modifyFacade: ModifyUserFacade,
) {

    @PatchMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "유저 수정 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = ModifyUserResponse::class))]
    )
    suspend fun modifyUser(principal: Principal, @Valid @RequestBody request: ModifyUserRequest):
            CommonResponse<ModifyUserResponse> {
        val response = modifyFacade.modifyUser(request, principal.name)
        return CommonResponse.success(response)
    }
}