package com.fone.filmone.presentation.profile

import com.fone.filmone.application.profile.RetrieveProfileWantFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.domain.common.Type
import com.fone.filmone.presentation.profile.RetrieveProfileWantDto.RetrieveProfileWantResponse
import io.swagger.annotations.Api
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal


@Api(tags = ["04. Profile Info"], description = "프로필 서비스")
@RestController
@RequestMapping("/api/v1/profiles")
class RetrieveProfileWantController(
    private val retrieveProfileWantFacade: RetrieveProfileWantFacade,
) {

    @GetMapping("/wants")
    @PreAuthorize("hasRole('USER')")
    suspend fun retrieveProfileWant(
        principal: Principal,
        @RequestParam type: Type,
    ): CommonResponse<RetrieveProfileWantResponse> {
        val response = retrieveProfileWantFacade.retrieveProfileWant(principal.name, type)

        return CommonResponse.success(response)
    }
}