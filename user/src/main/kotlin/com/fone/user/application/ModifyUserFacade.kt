package com.fone.user.application

import com.fone.user.domain.service.ModifyUserService
import com.fone.user.presentation.dto.ModifyUserDto.ModifyUserRequest
import org.springframework.stereotype.Service

@Service
class ModifyUserFacade(
    private val modifyUserService: ModifyUserService,
) {

    suspend fun modifyUser(request: ModifyUserRequest, email: String) = modifyUserService.modifyUser(request, email)
}
