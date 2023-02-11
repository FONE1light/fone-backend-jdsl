package com.fone.filmone.application.user

import com.fone.filmone.domain.user.service.LogOutUserService
import org.springframework.stereotype.Service

@Service
class LogOutUserFacade(
    private val logOutUserService: LogOutUserService,
) {

    suspend fun logOutUser(email: String) = logOutUserService.logOutUser(email)
}