package com.fone.filmone.application.user

import com.fone.filmone.domain.user.service.ModifyUserService
import com.fone.filmone.presentation.user.ModifyUserDto.ModifyUserRequest
import org.springframework.stereotype.Service

@Service
class ModifyUserFacade(
    private val modifyUserService: ModifyUserService,
) {

    suspend fun modifyUser(request: ModifyUserRequest) = modifyUserService.modifyUser(request)
}