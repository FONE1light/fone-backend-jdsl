package com.fone.user.application

import com.fone.user.domain.service.PasswordUpdateService
import com.fone.user.presentation.dto.PasswordUpdateDto
import org.springframework.stereotype.Service

@Service
class PasswordUpdateFacade(
    private val passwordUpdateService: PasswordUpdateService,
) {
    suspend fun update(request: PasswordUpdateDto.PasswordUpdateRequest) =
        passwordUpdateService.updateRequest(request)
}
