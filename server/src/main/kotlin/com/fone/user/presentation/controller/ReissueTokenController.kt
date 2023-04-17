package com.fone.user.presentation.controller

import com.fone.common.jwt.Token
import com.fone.common.response.CommonResponse
import com.fone.user.application.ReissueTokenFacade
import com.fone.user.presentation.dto.ReissueTokenDto
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.validation.Valid

@Api(tags = ["01. User Info"], description = "유저 서비스")
@RestController
@RequestMapping("/api/v1/users")
class ReissueTokenController(
    private val reissueTokenFacade: ReissueTokenFacade,
) {

    @PostMapping("/reissue")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "토큰 재발행 API")
    @ApiResponse(
        responseCode = "200",
        description = "성공",
        content = [Content(schema = Schema(implementation = Token::class))]
    )
    suspend fun reissueToken(
        @RequestBody @Valid
        request: ReissueTokenDto.ReissueTokenRequest,
        principal: Principal,
    ): CommonResponse<Token> {
        val response = reissueTokenFacade.reissueToken(request, principal.name)

        return CommonResponse.success(response)
    }
}
