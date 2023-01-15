package com.fone.filmone.presentation.user

import com.fone.filmone.application.user.SignOutUserFacade
import com.fone.filmone.common.response.CommonResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/v1/user")
class SignOutUserController(
    private val signOutUserFacade: SignOutUserFacade,
) {

    @PatchMapping("/sign-out")
    @PreAuthorize("hasRole('USER')")
    suspend fun signOutUser(principal: Principal): CommonResponse<Nothing?> {
        signOutUserFacade.signOutUser(principal.name)
        return CommonResponse.success(null, "회원탈퇴가 완료되었습니다.")
    }
}