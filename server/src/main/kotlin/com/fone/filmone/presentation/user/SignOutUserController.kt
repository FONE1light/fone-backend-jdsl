package com.fone.filmone.presentation.user

import com.fone.common.response.CommonResponse
import com.fone.filmone.application.user.SignOutUserFacade
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@Api(tags = ["01. User Info"], description = "유저 서비스")
@RestController
@RequestMapping("/api/v1/user")
class SignOutUserController(
    private val signOutUserFacade: SignOutUserFacade,
) {

    @PatchMapping("/sign-out")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "회원탈퇴 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
    )
    suspend fun signOutUser(principal: Principal): CommonResponse<Nothing?> {
        signOutUserFacade.signOutUser(principal.name)
        return CommonResponse.success(null, "회원탈퇴가 완료되었습니다.")
    }
}