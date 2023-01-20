package com.fone.filmone.presentation.user

import com.fone.filmone.application.user.RetrieveMyPageUserFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.presentation.user.RetrieveMyPageUserDto.RetrieveMyPageUserResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/v1/user")
class RetrieveMyPageUserController(
    private val retrieveMyPageUserFacade: RetrieveMyPageUserFacade,
) {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    suspend fun retrieveMyPageUser(principal: Principal):
            CommonResponse<RetrieveMyPageUserResponse> {
        val response = retrieveMyPageUserFacade.retrieveMyPageUser(principal.name)
        return CommonResponse.success(response)
    }
}