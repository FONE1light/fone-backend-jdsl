package com.fone.user.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.user.application.ModifyUserFacade
import com.fone.user.presentation.dto.AdminModifyUserRequest
import com.fone.user.presentation.dto.ModifyUserRequest
import com.fone.user.presentation.dto.ModifyUserResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.validation.Valid

@Api(tags = ["01. User Info"], description = "유저 서비스")
@RestController
@RequestMapping("/api/v1/users")
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
    suspend fun modifyUser(
        principal: Principal,
        @Valid @RequestBody
        request: ModifyUserRequest,
    ): CommonResponse<ModifyUserResponse> {
        val response = modifyFacade.modifyUser(request, principal.name)
        return CommonResponse.success(response)
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "유저 수정 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = ModifyUserResponse::class))]
    )
    suspend fun modifyUserById(
        @PathVariable
        id: Long,
        @Valid @RequestBody
        request: AdminModifyUserRequest,
    ): CommonResponse<ModifyUserResponse> {
        val response = modifyFacade.adminModifyUser(request, id)
        return CommonResponse.success(response)
    }
}
