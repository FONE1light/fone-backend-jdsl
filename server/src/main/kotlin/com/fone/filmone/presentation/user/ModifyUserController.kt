package com.fone.filmone.presentation.user

import com.fone.filmone.application.user.ModifyUserFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.presentation.user.ModifyUserDto.ModifyUserRequest
import com.fone.filmone.presentation.user.ModifyUserDto.ModifyUserResponse
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.validation.Valid

@RestController
@RequestMapping("/user/v1/user")
class ModifyUserController(
    private val modifyFacade: ModifyUserFacade,
) {

    @PatchMapping
    suspend fun modifyUser(principal: Principal, @Valid @RequestBody request: ModifyUserRequest):
            CommonResponse<ModifyUserResponse> {
        val response = modifyFacade.modifyUser(request, principal.name)
        return CommonResponse.success(response)
    }
}