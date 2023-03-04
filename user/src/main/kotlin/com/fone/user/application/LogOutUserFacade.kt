package com.fone.user.application

import com.fone.user.domain.service.LogOutUserService
import org.springframework.stereotype.Service

@Service
class LogOutUserFacade(
    private val logOutUserService: LogOutUserService,
) {

    suspend fun logOutUser(email: String) = logOutUserService.logOutUser(email)
}
