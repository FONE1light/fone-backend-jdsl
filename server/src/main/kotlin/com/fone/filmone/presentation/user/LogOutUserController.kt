package com.fone.filmone.presentation.user

import com.fone.common.response.CommonResponse
import com.fone.filmone.application.user.LogOutUserFacade
import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@Api(tags = ["01. User Info"], description = "유저 서비스")
@RestController
@RequestMapping("/api/v1/user")
class LogOutUserController(
    private val logOutUserFacade: LogOutUserFacade,
) {

    @PostMapping("/log-out")
    suspend fun logOut(principal: Principal): CommonResponse<Nothing?> {
        logOutUserFacade.logOutUser(principal.name)

        return CommonResponse.success(null, "로그아웃 되었습니다.")
    }
}