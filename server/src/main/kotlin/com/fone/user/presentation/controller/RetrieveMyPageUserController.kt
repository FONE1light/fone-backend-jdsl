package com.fone.user.presentation.controller

import com.fone.common.response.CommonResponse
import com.fone.user.application.RetrieveMyPageUserFacade
import com.fone.user.presentation.dto.RetrieveMyPageUserDto.RetrieveMyPageUserResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@Api(tags = ["01. User Info"], description = "유저 서비스")
@RestController
@RequestMapping("/api/v1/users")
class RetrieveMyPageUserController(
    private val retrieveMyPageUserFacade: RetrieveMyPageUserFacade,
) {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "마이페이지 조회 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = RetrieveMyPageUserResponse::class))]
    )
    suspend fun retrieveMyPageUser(
        principal: Principal,
    ): CommonResponse<RetrieveMyPageUserResponse> {
        val response = retrieveMyPageUserFacade.retrieveMyPageUser(principal.name)
        return CommonResponse.success(response)
    }
}
