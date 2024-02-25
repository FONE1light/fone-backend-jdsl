package com.fone.user.application

import com.fone.common.jwt.Token
import com.fone.user.domain.service.ReissueTokenService
import com.fone.user.domain.service.SignInUserService
import com.fone.user.presentation.dto.ReissueTokenRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReissueTokenFacade(
    private val reissueTokenService: ReissueTokenService,
    private val signInUserService: SignInUserService,
) {

    @Transactional
    suspend fun reissueToken(request: ReissueTokenRequest): Token {
        val token = reissueTokenService.reissueToken(request)
        signInUserService.signInUser(token)
        return token
    }
}
