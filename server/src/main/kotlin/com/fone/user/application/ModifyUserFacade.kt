package com.fone.user.application

import com.fone.user.domain.service.ModifyUserService
import com.fone.user.presentation.dto.AdminModifyUserRequest
import com.fone.user.presentation.dto.ModifyUserRequest
import org.springframework.stereotype.Service

@Service
class ModifyUserFacade(
    private val modifyUserService: ModifyUserService,
) {

    suspend fun modifyUser(request: ModifyUserRequest, email: String) = modifyUserService.modifyUser(request, email)
    suspend fun adminModifyUser(request: AdminModifyUserRequest, id: Long) =
        modifyUserService.adminModifyUser(request, id)
}
