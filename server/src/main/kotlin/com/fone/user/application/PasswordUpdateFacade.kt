package com.fone.user.application

import com.fone.user.domain.service.PasswordUpdateService
import com.fone.user.presentation.dto.PasswordUpdateRequest
import org.springframework.stereotype.Service

@Service
class PasswordUpdateFacade(
    private val passwordUpdateService: PasswordUpdateService,
) {
    suspend fun updatePassword(request: PasswordUpdateRequest) =
        passwordUpdateService.updatePassword(request)
}
