package com.fone.user.application

import com.fone.user.domain.service.ReissueTokenService
import com.fone.user.presentation.dto.ReissueTokenDto
import org.springframework.stereotype.Service

@Service
class ReissueTokenFacade(
    private val reissueTokenService: ReissueTokenService,
) {

    suspend fun reissueToken(request: ReissueTokenDto.ReissueTokenRequest) =
        reissueTokenService.reissueToken(request)
}
